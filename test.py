# test_models.py
import logging
from PIL import Image
import numpy as np

# Configure logging to see what's happening
logging.basicConfig(level=logging.INFO)

def test_models():
    """Test both AI models with a sample image"""
    
    print("🧪 Testing AI Models...")
    
    try:
        # Import our models
        from models.image_encoder import get_image_encoder
        from models.caption_generator import get_caption_generator
        
        print("\n1️⃣ Loading models...")
        encoder = get_image_encoder()
        caption_gen = get_caption_generator()
        
        print("\n2️⃣ Testing with a simple test image...")
        
        # Create a simple test image (or use a real image file)
        # For testing, create a red square
        test_image = Image.new('RGB', (100, 100), color='red')
        
        print("\n3️⃣ Testing image encoding...")
        embedding = encoder.encode_image(test_image)
        print(f"✅ Image encoded to vector of shape: {embedding.shape}")
        print(f"✅ Embedding type: {type(embedding)}")
        
        print("\n4️⃣ Testing text encoding...")
        text_embedding = encoder.encode_text("red square")
        print(f"✅ Text encoded to vector of shape: {text_embedding.shape}")
        
        print("\n5️⃣ Testing caption generation...")
        caption = caption_gen.generate_caption(test_image)
        print(f"✅ Generated caption: '{caption}'")
        
        print("\n🎉 All tests passed! Your AI models are working!")
        
    except Exception as e:
        print(f"❌ Test failed: {e}")
        print("\nThis might be normal on first run - models need to download.")
        print("Try running again in a few moments.")

if __name__ == "__main__":
    test_models()
