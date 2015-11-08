# A script to start a tmux vap prototype node server session for demo-ing.
# RedHat, AmazonEC2
tmux new-session -d -s vapserver
tmux send-keys -t vapserver:0 "cd /home/ec2-user/vap/server/" C-m
tmux send-keys -t vapserver:0 "node server.js" C-m