import os
import numpy as np
from pathlib import Path
from models.image_encoder import get_image_encoder

# Initialize the image encoder singleton
encoder = get_image_encoder()

# Set your image dataset folder path below
image_folder = Path(r"C:\Users\ADMIN\OneDrive\Pictures")  # ← update this to your folder

# Collect all image files (jpg, jpeg, png, bmp)
image_paths = [p for p in image_folder.iterdir() 
               if p.suffix.lower() in ['.jpg', '.jpeg', '.png', '.bmp']]

embeddings = []
filenames = []

# Encode each image and collect embeddings/filenames
for image_path in image_paths:
    embedding = encoder.encode_image(str(image_path))
    embeddings.append(embedding)
    filenames.append(image_path.name)

# Stack embeddings into a numpy array
embeddings = np.vstack(embeddings)

# Save embeddings and filenames
np.save('embeddings.npy', embeddings)

import json
with open('image_files.json', 'w') as f:
    json.dump(filenames, f)

print(f"Precomputed and saved embeddings for {len(filenames)} images.")
