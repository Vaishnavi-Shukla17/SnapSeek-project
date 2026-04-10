import os
import json
import numpy as np
from PIL import Image
import torch
from models.image_encoder import get_image_encoder

def generate_embeddings(folder_path):
    """Generate embeddings.npy and image_files.json from folder"""
    encoder = get_image_encoder()
    image_files = []
    embeddings = []
    
    # Find all images in folder
    image_extensions = ('.png', '.jpg', '.jpeg', '.webp', '.bmp')
    for filename in sorted(os.listdir(folder_path)):
        if filename.lower().endswith(image_extensions):
            img_path = os.path.join(folder_path, filename)
            try:
                # Encode image
                embedding = encoder.encode_image(img_path)
                embeddings.append(embedding)
                image_files.append(filename)  # Store just filename
                print(f"Encoded: {filename}")
            except Exception as e:
                print(f"Failed to encode {filename}: {e}")
    
    # Save
    embeddings_array = np.array(embeddings)
    np.save('embeddings.npy', embeddings_array)
    
    with open('image_files.json', 'w') as f:
        json.dump(image_files, f)
    
    print(f"Saved {len(image_files)} images: embeddings.npy, image_files.json")

if __name__ == "__main__":
    folder_path = input("Enter folder path: ").strip()
    if os.path.isdir(folder_path):
        generate_embeddings(folder_path)
    else:
        print("Invalid folder!")