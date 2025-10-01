# models/caption_generator.py
import torch
import logging
from transformers import BlipProcessor, BlipForConditionalGeneration
from PIL import Image
from typing import Union, Dict
from pathlib import Path
from config import settings

logger = logging.getLogger(__name__)

# Short, prefix-style cues work best for BLIP conditional captioning
STYLE: Dict[str, str] = {
    "funny": "Funny caption:",
    "playful": "Playful caption:",
    "professional": "Professional caption:",
    "romantic": "Romantic caption:",
}

# Optional aliases so friendly inputs still hit a supported style
ALIAS: Dict[str, str] = {
    "love": "romantic",
    "loving": "romantic",
}

class CaptionGenerator:
    """
    Handles image captioning using BLIP with optional tone conditioning
    """

    def __init__(self):
        self.device = self._setup_device()
        self.processor = None
        self.model = None
        self.load_model()

    def _setup_device(self):
        """Choose GPU or CPU automatically"""
        if settings.device == "auto":
            return torch.device("cuda" if torch.cuda.is_available() else "cpu")
        return torch.device(settings.device)

    def load_model(self):
        """Load BLIP model once at startup"""
        try:
            logger.info(f"Loading BLIP model on {self.device}")
            self.processor = BlipProcessor.from_pretrained("Salesforce/blip-image-captioning-base")
            self.model = BlipForConditionalGeneration.from_pretrained(
                "Salesforce/blip-image-captioning-base"
            ).to(self.device)
            self.model.eval()
            logger.info("BLIP model loaded successfully")
        except Exception as e:
            logger.error(f"Failed to load BLIP model: {e}")
            raise RuntimeError(f"Could not initialize caption generator: {e}")

    def generate_caption(self, image: Union[str, Path, Image.Image], tone: str | None = None) -> str:
        """
        Generate a caption for an image, optionally conditioned on tone.
        Uses short style prompts, sampling, and prompt-slicing to ensure tone control.
        """
        try:
            # Handle different input types
            if isinstance(image, (str, Path)):
                pil_image = Image.open(image).convert("RGB")
            else:
                pil_image = image

            # Normalize and map tone to supported keys
            style_key = (tone or "").lower()
            style_key = ALIAS.get(style_key, style_key)
            instruction = STYLE.get(style_key, "A caption:")

            # Conditional captioning via short prompt prefix
            inputs = self.processor(images=pil_image, text=instruction, return_tensors="pt").to(self.device)

            # Use sampling so tone influences token choice
            with torch.no_grad():
                output_ids = self.model.generate(
                    **inputs,
                    max_new_tokens=40,     # allow enough room for stylistic expression
                    do_sample=True,        # enable sampling (required for temperature/top_p to take effect)
                    temperature=1.0,       # stylistic variability
                    top_p=0.95,            # nucleus sampling
                    repetition_penalty=1.1 # discourage verbatim loops
                )

            # Slice off the prompt so only newly generated caption text is returned
            generated = output_ids[0]
            if "input_ids" in inputs:
                prompt_len = inputs["input_ids"].shape[1]
                generated = generated[prompt_len:]

            caption = self.processor.decode(generated, skip_special_tokens=True).strip()
            return caption or "Caption unavailable."
        except Exception as e:
            logger.error(f"Failed to generate caption: {e}")
            return f"Caption generation failed: {str(e)}"


# Global instance - loaded once and reused
_caption_generator = None

def get_caption_generator():
    """Get the global caption generator instance"""
    global _caption_generator
    if _caption_generator is None:
        _caption_generator = CaptionGenerator()
    return _caption_generator
