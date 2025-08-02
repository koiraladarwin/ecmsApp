
import pandas as pd
import requests
import os
from typing import Optional
import cloudinary
import cloudinary.uploader

cloudinary.config(
    cloud_name="dcvr2byrp",
    api_key="634395575667449",
    api_secret="89DqsMl5tyM-S-acNse9VSr-wY8",
    secure=True
)

EVENT_ID = "ae631470-937c-4f25-8147-481beb9b6b45" 

def upload_image(image_path: str) -> Optional[str]:
    try:
        result = cloudinary.uploader.upload(image_path)
        return result.get("secure_url")
    except Exception as e:
        print(f"Upload failed for {image_path}: {e}")
        return None

df = pd.read_excel('data.xlsx', engine='openpyxl')

for index, row in df.iterrows():
    print(f"Processing Row {index}...")

    image_filename = f"{index+1}.jpg"
    local_image_path = os.path.join("photo", image_filename)

    if not os.path.exists(local_image_path):
     print(f"Image not found: {local_image_path}, using default.jpg instead.")
     local_image_path = "default.jpg"

    image_url = upload_image(local_image_path)
    if not image_url:
        print(f"sskipping user due to upload failure: {row['Name']}")
        continue

    user_payload = {
        "full_name": row["Name"],
        "company": row["Organization"],
        "image_url": image_url,
        "role": row["Category"],
        "position": row["Designation"],
    }

    try:
        headers = {
            "Authorization": "Bearer eyJhbGciOiJSUzI1NiIsImtpZCI6ImE4ZGY2MmQzYTBhNDRlM2RmY2RjYWZjNmRhMTM4Mzc3NDU5ZjliMDEiLCJ0eXAiOiJKV1QifQ.eyJuYW1lIjoiZGFyd2luIiwicGljdHVyZSI6Imh0dHBzOi8vbGgzLmdvb2dsZXVzZXJjb250ZW50LmNvbS9hL0FDZzhvY0twcXdKd0hjTGZCd1EyMHQyWHQ4QVhoVWF6SFZTdDhXY3ZQXzJnVDlJN3lXdUlZdz1zOTYtYyIsImlzcyI6Imh0dHBzOi8vc2VjdXJldG9rZW4uZ29vZ2xlLmNvbS9lY21zLTk0MjVlIiwiYXVkIjoiZWNtcy05NDI1ZSIsImF1dGhfdGltZSI6MTc1Mjk0MTM3MSwidXNlcl9pZCI6IkIzN0R5all2a1JUcXRENlBiNGM1d2ZjTzJ6YzIiLCJzdWIiOiJCMzdEeWpZdmtSVHF0RDZQYjRjNXdmY08yemMyIiwiaWF0IjoxNzUzMDk3MTQwLCJleHAiOjE3NTMxMDA3NDAsImVtYWlsIjoiZGFyd2lua29pcmFsYTEyM0BnbWFpbC5jb20iLCJlbWFpbF92ZXJpZmllZCI6dHJ1ZSwiZmlyZWJhc2UiOnsiaWRlbnRpdGllcyI6eyJnb29nbGUuY29tIjpbIjExMTcyMDU4MTEyODE5NDA3NTgxNiJdLCJlbWFpbCI6WyJkYXJ3aW5rb2lyYWxhMTIzQGdtYWlsLmNvbSJdfSwic2lnbl9pbl9wcm92aWRlciI6Imdvb2dsZS5jb20ifX0.chQ8tc4mn9xCgnDS8J_yGsxrvsoUBzhuLk_wSs471WYiiF7Vybem50fj31Xp6FTtDIkGZPoJyVhf5B0aMeVe3K8jwUsL969uYgf8ijFs4gv8ZUfrcWHXkc9UIWrgTat22hOTUFLYVyI7ZDs_8Ugs_bo0AM73Ev2IXKxYMS9L528Zxv7XUKKe8ZLNdh1vfGhNZgFL6hrpLTwTxkK-eB4Y1qFQMj28hSz3J9sZAVgxAg5MBowUZWVASy9kw_jJ-AUAqTMYH65XAw2CUJTi3pTY2KPzBTrX2beKBZTOvzfzOKL_VYWu_jpJvRi75q4DGOVJ3WAS-5sbfrJCrn06ni16Sw",
            "Content-Type": "application/json"
        }
        # response = requests.post("https://scanin-production-ca05.up.railway.app/user", json=user_payload, headers=headers)
        response = requests.post("http://localhost:4000/user", json=user_payload, headers=headers)
        print(f" -> {response.status_code} | {response.text}")

        if response.status_code == 201:
            user_id = response.json().get("id")
            if user_id:
                attendee_payload = {
                    "user_id": user_id,
                    "event_id": EVENT_ID,
                    "role": row["Category"]
                }
                # attendee_response = requests.post("https://scanin-production-ca05.up.railway.app/attendees", json=attendee_payload,headers=headers)
                attendee_response = requests.post("http://localhost:4000/attendees", json=attendee_payload,headers=headers)
                print(f"ttendee registration: {attendee_response.status_code} | {attendee_response.text}")
            else:
                print(" no 'id' found in user creation response.")
    except Exception as e:
        print(f"failed to POST user {row['Name']}: {e}")

