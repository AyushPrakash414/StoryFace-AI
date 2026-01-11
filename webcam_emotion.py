import cv2
import numpy as np
from tensorflow.keras.models import model_from_json

with open("emotiondetector.json", "r") as json_file:
    model_json = json_file.read()

model = model_from_json(model_json)
model.load_weights("emotiondetector.weights.h5")

print("✅ Model loaded successfully")


# Load Haar cascade
haar_file = cv2.data.haarcascades + "haarcascade_frontalface_default.xml"
face_cascade = cv2.CascadeClassifier(haar_file)

# Emotion labels
labels = ['angry','disgust','fear','happy','neutral','sad','surprise']

# Preprocess face image
def extract_features(image):
    image = cv2.resize(image, (48, 48))
    image = image.reshape(1, 48, 48, 1)
    return image / 255.0

# Start webcam
webcam = cv2.VideoCapture(0)

if not webcam.isOpened():
    print("❌ Webcam not accessible")
    exit()

while True:
    ret, frame = webcam.read()
    if not ret:
        break

    gray = cv2.cvtColor(frame, cv2.COLOR_BGR2GRAY)

    # 🔥 FIX: use gray image for detection
    faces = face_cascade.detectMultiScale(
        gray,
        scaleFactor=1.3,
        minNeighbors=5
    )

    for (x, y, w, h) in faces:
        face = gray[y:y+h, x:x+w]

        img = extract_features(face)
        pred = model.predict(img, verbose=0)
        label = labels[np.argmax(pred)]

        # Draw rectangle + label
        cv2.rectangle(frame, (x,y), (x+w,y+h), (255,0,0), 2)
        cv2.putText(
            frame,
            label,
            (x, y-10),
            cv2.FONT_HERSHEY_SIMPLEX,
            0.9,
            (0,0,255),
            2
        )

    cv2.imshow("Emotion Detector", frame)

    # Press ESC to exit
    if cv2.waitKey(1) & 0xFF == 27:
        break

webcam.release()
cv2.destroyAllWindows()
