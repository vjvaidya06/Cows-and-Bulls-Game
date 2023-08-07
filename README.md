# Cows-and-Bulls-Game
A simple Cows and Bulls game written in Java.

Cows and Bulls is a word game similar to Wordle. A word is picked, and you guess a word of the same length. The game will then return the numbers of cows and bulls. Bulls are letters that are correct and in the right spot, and cows that are letter that are right but in the wrong spot. A bull cannot also be a cow. You don't know which letter is a cow or bull, only that a number of cows and bulls exist. Singleplayer mode picks a random word of a chosen length from a dictionary. Multiplayer mode involves a person inputting a word as a command line argument and then giving it to another person for them to guess. To play this game, run it like a regular java file, and it will give you instructions to proceed. Make sure the dictionary and common english words files are in the same directory. If you wish to preselect a word for your friend to guess, input it as a command line argument.

LAN PLAY INSTRUCTIONS
To play this with friends, you have to be on the same network (unless you choose to port forward). The person who creates the word is the server, they must select the option first. The person who guesses the word is the client. They should select the option after the server has already been started. Port must be the same between the two. As for IP, the person creating the word needs to find their IPv4 address and the person guessing needs to input it (If you're playing in two different windows on the same computer, the default configuration localhost should work just fine). On Windows, type "ipconfig" in cmd or powershell, and the IPv4 will be under Wifi or Ethernet.
![image](https://github.com/vjvaidya06/Cows-and-Bulls-Game/assets/108746579/5211efdf-3223-4204-a0ca-d923c50c40ee)

On Linux go into the terminal and type hostname -I. If this doesn't work, type ifconfig.

![image](https://github.com/vjvaidya06/Cows-and-Bulls-Game/assets/108746579/22f0174f-d6aa-443d-80ad-1b71b090886f)


On Mac, it can be found in settings under network labeled "IP Address".

![image](https://github.com/vjvaidya06/Cows-and-Bulls-Game/assets/108746579/425150b2-54bb-4a0e-8221-872fe0ab0ef5)
![image](https://github.com/vjvaidya06/Cows-and-Bulls-Game/assets/108746579/f16749c0-3cda-4352-8f4e-2ea1cc49cf91)

Once you are connected, server will get a message that the client has connected. After that, the server needs to type in the word and the guess limit (or a negative number if you wish to leave it unlimited). Once that is done the client can begin guessing. The client's guesses will also be sent to the server.
