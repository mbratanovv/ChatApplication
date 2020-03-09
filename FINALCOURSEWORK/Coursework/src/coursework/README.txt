Network Distributed System
By :
Mario Bratanov - 001027503
Mahamud Hussein - 001015542
Jordan Trinder - 000978834 
Jack McCabe - 001034199

TO DO:
Mario :
- finish comments
- JUnit testing
- bug fixing and making code better
for the coordinator :
      - use the working functionality about the list of users currently online
      - assign a boolean variable to the first user that connects (use the arraylist to get its first value(first user connected)) and while he is online - the boolean variable is true;
      - when he disconnects, the next first user in the list will become coordinator (the loop will look through the arraylist each time a new user connects?)
      - only the coordinator can use the "online" function ?, if not coordinator - message pop up that he is not authorised to use this command
Jordan:
- Find design that fits code
- make username unique
- bug fixing and making code better
WHAT's DONE : 
- Make sure username is unique (check the arraylist for the same username - if the same, then pop up message prompting for a new one, because this is existing)
- finish comments
- clean up code optimise 
