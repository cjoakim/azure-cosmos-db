# Start the Vehicle Transponder Telemetry sender process.
# Chris Joakim, Microsoft, March 2023

echo 'activating the python virtual environment...'
.\venv\Scripts\Activate.ps1

echo 'create the vehicle activity stream...'
python main.py create_vehicle_activity_stream 0.5 100
