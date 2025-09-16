# config.py
from pathlib import Path
from pydantic_settings import BaseSettings, SettingsConfigDict  # Pydantic v2 settings [1]

class Settings(BaseSettings):
    # Server
    host: str = "127.0.0.1"
    port: int = 8000
    debug: bool = True

    # Paths
    base_dir: Path = Path(__file__).parent
    data_dir: Path = base_dir / "data"
    models_dir: Path = data_dir / "models"
    index_dir: Path = data_dir / "index"
    images_dir: Path = data_dir / "images"

    # AI models
    device: str = "auto"
    clip_model_name: str = "openai/clip-vit-base-patch32"
    blip_model_name: str = "Salesforce/blip-image-captioning-base"

    # Performance
    batch_size: int = 16
    max_results: int = 50
    similarity_threshold: float = 0.1
    max_file_size: int = 10 * 1024 * 1024  # 10 MB

    # Pydantic v2 settings config (replaces class Config in v1)
    model_config = SettingsConfigDict(env_file=".env", extra="ignore")  # loads .env, ignore extras [1][3]

    def create_directories(self) -> None:
        for d in (self.data_dir, self.models_dir, self.index_dir, self.images_dir):
            d.mkdir(parents=True, exist_ok=True)

# Global instance
settings = Settings()
settings.create_directories()
