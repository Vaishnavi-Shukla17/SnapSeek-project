# models/caption_generator.py
import torch
import logging
from transformers import BlipProcessor, BlipForConditionalGeneration
from PIL import Image
from typing import Union
from pathlib import Path
from config import settings
from typing import Dict

STYLE: Dict[str, str] = {
    "funny": "Write a witty, joke‑forward caption with a playful punchline; keep it light and humorous.",
    "playful": "Write an upbeat, emoji‑friendly caption with energetic wording and a cheerful vibe.",
    "professional": "Write a concise, polished caption with clear, formal language and no slang.",
    "romantic": "Write a warm, affectionate caption with tender phrasing and gentle imagery.",
}

def generate_caption(image_path: str, tone: str) -> str:
    instruction = STYLE.get(tone, "Write a neutral, descriptive caption.")
    prompt = f"{instruction} Describe the image at path: {image_path}. Keep it under 20 words."
    # Example: using a transformers pipeline (adjust to the model actually used)
    from transformers import pipeline
    generator = pipeline("text-generation", model="openai-community/gpt2")
    out = generator(
        prompt,
        max_new_tokens=40,
        do_sample=True,          # enable sampling so tone affects token choice
        temperature=0.9,         # increase variation
        top_p=0.9,               # nucleus sampling
        repetition_penalty=1.1,  # discourage repeats slightly
    )[0]["generated_text"]
    # Post-process: return only the new text after the prompt
    return out[len(prompt):].strip() or "Caption unavailable."



class CaptionGenerator:
    """
    Handles image captioning using BLIP model
    Generates human-readable descriptions of images
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
        """Load BLIP model - this happens once at startup"""
        try:
            logger.info(f"Loading BLIP model on {self.device}")
            
            # Load BLIP processor (handles image preprocessing)
            self.processor = BlipProcessor.from_pretrained(
                "Salesforce/blip-image-captioning-base"
            )
            
            # Load BLIP model (generates captions)
            self.model = BlipForConditionalGeneration.from_pretrained(
                "Salesforce/blip-image-captioning-base"
            ).to(self.device)
            
            # Set to evaluation mode (not training)
            self.model.eval()
            
            logger.info("✅ BLIP model loaded successfully!")
            
        except Exception as e:
            logger.error(f"❌ Failed to load BLIP model: {e}")
            raise RuntimeError(f"Could not initialize caption generator: {e}")
    
    def generate_caption(self, image: Union[str, Path, Image.Image]) -> str:
        """
        Generate a caption for an image
        Returns a human-readable description
        """
        try:
            # Handle different input types
            if isinstance(image, (str, Path)):
                pil_image = Image.open(image).convert('RGB')
            else:
                pil_image = image
            
            # Preprocess image for BLIP
            inputs = self.processor(images=pil_image, return_tensors="pt").to(self.device)
            
            # Generate caption
            with torch.no_grad():
                output_ids = self.model.generate(
                    **inputs,
                    max_length=50,      # Maximum caption length
                    num_beams=5,        # Quality vs speed (higher = better quality)
                    early_stopping=True
                )
            
            # Decode the generated tokens to text
            caption = self.processor.decode(output_ids[0], skip_special_tokens=True)
            return caption
            
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
