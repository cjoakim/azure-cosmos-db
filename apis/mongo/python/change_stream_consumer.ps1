# Consume the Vehicle Transponder Telemetry change-stream.
# Chris Joakim, Microsoft, March 2023

echo 'activating the python virtual environment...'
.\venv\Scripts\Activate.ps1

echo 'consume the vehicle activity stream...'
python change_stream_consumer.py consume dev vehicle_activity
