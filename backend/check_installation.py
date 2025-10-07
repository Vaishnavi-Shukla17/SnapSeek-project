# check_installation.py
import sys

def check_library(name, import_name=None):
    """Check if a library is installed and show version"""
    if import_name is None:
        import_name = name
    
    try:
        module = __import__(import_name)
        version = getattr(module, '__version__', 'Unknown version')
        print(f"✅ {name}: {version}")
        return True
    except ImportError:
        print(f"❌ {name}: NOT INSTALLED")
        return False

print("=== Checking Library Installation ===\n")

# Critical libraries
print("🔥 CRITICAL LIBRARIES:")
check_library("PyTorch", "torch")
check_library("TorchVision", "torchvision") 
check_library("Transformers", "transformers")
check_library("FAISS", "faiss")

print("\n🖼️ IMAGE PROCESSING:")
check_library("Pillow", "PIL")
check_library("OpenCV", "cv2")
check_library("NumPy", "numpy")

print("\n🌐 WEB FRAMEWORK:")
check_library("FastAPI", "fastapi")
check_library("Uvicorn", "uvicorn")

print("\n🗄️ DATA & UTILITIES:")
check_library("SQLAlchemy", "sqlalchemy")
check_library("Pydantic", "pydantic")
check_library("Python-DotEnv", "dotenv")
check_library("AIOFiles", "aiofiles")
check_library("Sentence-Transformers", "sentence_transformers")
check_library("Python-Multipart", "multipart")

print("\n=== Installation Check Complete ===")
