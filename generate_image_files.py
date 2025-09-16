import os
import json
from pathlib import Path

image_folder = Path("C:/Users/ADMIN/OneDrive/Pictures")  # ← Make sure this matches your image folder path
filenames = [
    f.name for f in image_folder.glob("*") 
    if f.suffix.lower() in {".jpg", ".png", ".jpeg", ".bmp"}
]

with open('image_files.json', 'w') as f:
    json.dump(filenames, f)

print(f"Generated image_files.json with {len(filenames)} filenames.")
