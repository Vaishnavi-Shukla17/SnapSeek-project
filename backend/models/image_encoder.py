# models/image_encoder.py
import torch
import logging
import numpy as np
from PIL import Image
from sentence_transformers import SentenceTransformer
from typing import Union, List
from pathlib import Path
from config import settings

logger = logging.getLogger(__name__)

class ImageEncoder:
    """
    Handles image and text encoding using CLIP model
    This is the heart of your search functionality
    """
    
    def __init__(self):
        self.device = self._setup_device()
        self.model = None
        self.load_model()
        
    def _setup_device(self):
        """Choose GPU or CPU automatically"""
        if settings.device == "auto":
            return torch.device("cuda" if torch.cuda.is_available() else "cpu")
        return torch.device(settings.device)
    
    def load_model(self):
        """Load CLIP model - this happens once at startup"""
        try:
            logger.info(f"Loading CLIP model on {self.device}")
            
            # Load the CLIP model via sentence-transformers (easier API)
            self.model = SentenceTransformer('clip-ViT-B-32')
            self.model.to(self.device)
            
            logger.info("✅ CLIP model loaded successfully!")
            
        except Exception as e:
            logger.error(f"❌ Failed to load CLIP model: {e}")
            raise RuntimeError(f"Could not initialize image encoder: {e}")
    
    def encode_image(self, image: Union[str, Path, Image.Image]) -> np.ndarray:
        """
        Convert an image to a vector embedding
        This vector represents the 'meaning' of the image
        """
        try:
            # Handle different input types (file path or PIL image)
            if isinstance(image, (str, Path)):
                pil_image = Image.open(image).convert('RGB')
            else:
                pil_image = image
            
            # Convert image to vector using CLIP
            embedding = self.model.encode(pil_image)
            return embedding.astype(np.float32)
            
        except Exception as e:
            logger.error(f"Failed to encode image: {e}")
            raise
    
    def encode_text(self, text: str) -> np.ndarray:
        """
        Convert text query to a vector embedding
        This enables text-to-image search!
        """
        try:
            embedding = self.model.encode(text)
            return embedding.astype(np.float32)
            
        except Exception as e:
            logger.error(f"Failed to encode text '{text}': {e}")
            raise

# Global instance - loaded once and reused
_image_encoder = None

def get_image_encoder():
    """Get the global image encoder instance"""
    global _image_encoder
    if _image_encoder is None:
        _image_encoder = ImageEncoder()
    return _image_encoder
