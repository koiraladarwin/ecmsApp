from typing import Optional
import cloudinary
import cloudinary.uploader
from cloudinary.utils import cloudinary_url

cloudinary.config( 
    cloud_name = "dcvr2byrp", 
    api_key = "634395575667449", 
    api_secret = "89DqsMl5tyM-S-acNse9VSr-wY8", 
    secure=True
)


def upload_image(image_path: str) -> Optional[str]:
    try:
        result = cloudinary.uploader.upload(image_path)
        return result.get("secure_url")
    except Exception as e:
        print(f"Upload failed for {image_path}: {e}")
        return None

