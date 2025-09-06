# SnapSeek-project
B.Tech project: Local Image Search Engine combining JavaFX for UI and Python for AI/ML. Enables users to search and retrieve images from their system using natural language queries.
#Project Overview
This system combines state-of-the-art AI models (CLIP & BLIP) with high-performance vector search (FAISS) to create a desktop application that can:
- Search images using natural language: Find photos of cats sitting on tables
- Generate automatic captions: A orange tabby cat sitting on a wooden dining table
- Work completely offline: No data leaves your device, ever
- Scale to large collections: Handle 100,000+ images with sub-second search

# Technology Stack

| **Component** | **Technology** | **Purpose** |
|---------------|----------------|-------------|
| **AI Framework** | PyTorch 2.1.0 | Deep learning model execution |
| **Image Understanding** | OpenAI CLIP | Text-to-image similarity matching |
| **Caption Generation** | Salesforce BLIP | Automatic image description |
| **Vector Search** | Meta FAISS | High-performance similarity search |
| **Web Framework** | FastAPI | REST API with automatic documentation |
| **Data Validation** | Pydantic | Type-safe configuration and requests |
| **Image Processing** | Pillow + OpenCV | Image loading and preprocessing |
| **Database** | SQLite + SQLAlchemy | Metadata storage and management |
