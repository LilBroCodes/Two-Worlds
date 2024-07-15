#!/usr/bin/env zsh

# shellcheck disable=SC2162

# Function to check if the server is running
is_server_running() {
    pgrep -f "server.jar nogui" > /dev/null 2>&1
}

# Function to kill all instances of the server
kill_all_server_instances() {
    pkill -f "server.jar nogui"
}

# Check if the server is already running
if is_server_running; then
    echo "Server is already running. Killing existing instance."
    kill_all_server_instances
fi

mvn package || exit 1
cp target/TwoWorlds-1.0.jar server/plugins || exit 1
cd server || exit 1
java -jar server.jar nogui
echo "Press any key to continue..."
read -n 1
exit 0