# A Clever Word Game 

## Alex Piarkouski - Personal Project - Task 1

A Clever Word Game is a single-player game that combines elements of popular games like *Anagrams* and *Scrabble*. Similarly to *Scrabble* every letter in the latin alphabet is assigned a numeric point value based on its rarity and difficulty to incorporate in english-language words.

Example: 
- letter **A** is worth 1 point.
- letter **M** is worth 3 points.
- letter **X** is worth 8 points.

The player is then provided a word requirement - a framework for a type of word consisting of number of letters and optional special requirements like what letter has to be in which position. Some examples include "***4**-letter word" or "***6**-letter word that starts with letter **A***". The player must then provide a >=0 number of valid English words in the given restrictions - for example the player has **x** attempts. The player uses the keyboard to input a single word, then presses enter. The program then proceeds to evaluate whether the input word is a valid word in the english language and fits the requirements. For example if the player is asked for a 4-letter word the program will accept "bike" and "lake" but will not accept "sea" (not enough letters), "river" (too many letters) or "abcd" (not a valid word). If the word is a valid word then the program sums the point value of every letter in the word and adds that value to a player's existing score. If the word is invalid the player receives no points for their attempt and is invited to try again. Word validity will be checked against one of numerous collections of officially-recognized english-language words available for download online. If checking against 100,000+ words proves to be too time- or resource-consuming the program can be scaled back to only search in a smaller set - valid animal names for example - only a few thousand. The aim of the game is to record the highest score given constraints given by the program. Game is over when the player is out of attempts or chooses to exit the game. At the end of the round the player is provided the list of words they input along individual scores for each word, and their final round score.

The game is meant to be an engaging game-form exercise for a wide audience willing to challenge themselves. Different types of both single-player and multiplayer word games have been very popular in the past, attracting a very wide audience of all ages. I believe the game could be **especially popular** with 3 audiences:
- Young adults who spend a lot of time playing casual style games on their electronic devices (ex. GamePigeon, Scrabble, Crosswords)
- English Language Learners - the game serves as a good opportunity to practice newly acquired vocabulary in a fun and engaging setting.
- Linguistics enthusiasts, aspiring writers, anyone that prides themselves on having a rich vocabulary and is willing to challenge themselves.

As for my interest in the project, I consider myself a member of all 3 groups mentioned above. The idea for the project came to me during a game of *Anagrams* on my phone, which accurately shows how much I enjoy word games. English is also my third language and word games have been very helpful in practicing newly acquired vocabulary and learning new words for me. Finally I write a bit in my free time, so challenging myself in the vocabulary regard sounds interesting to me. This game is a fun, useful, and relatively complex project that can be complicated and improved almost infinitely, adding new restriction, levels, game modes, and visual improvements. All things mentioned have challenged me to attempt this project.    

## User Stories
**Phase 1**
- As a user, I would like to be able to input and view multiple word entries in one round 
- As a user, I would like to be able to exit a game 
- As a user, I would like to be able to view my total score, after the game round is finished  
- As a user, I would like to be able to see the list of input words along with their corresponding point values at the end of the game round.

**Phase 2**
- As a user, I want to be able to save my last score and last list of input words along with their corresponding point values to file
- As a user, I want to be able to be able to load my last saved score and last saved list of input words along with their corresponding point values from file. 

**Phase 4: Task 2**

Thu Nov 25 19:14:45 PST 2021
Valid word entry fish, 10 added to list of valid entries

Thu Nov 25 19:14:48 PST 2021
Valid word entry carp, 8 added to list of valid entries

Thu Nov 25 19:14:50 PST 2021
Valid word entry crab, 8 added to list of valid entries

Thu Nov 25 19:14:55 PST 2021
Game reset. List of valid entries reset

Thu Nov 25 19:14:57 PST 2021
Valid word entry cats, 6 added to list of valid entries

Thu Nov 25 19:14:59 PST 2021
Valid word entry dogs, 6 added to list of valid entries

Thu Nov 25 19:15:00 PST 2021
Valid word entry mice, 8 added to list of valid entries

Thu Nov 25 19:15:02 PST 2021
Valid word entry cars, 6 added to list of valid entries

Thu Nov 25 19:15:11 PST 2021
Game loaded from file. List of valid entries reset

Thu Nov 25 19:15:11 PST 2021
Valid word entry more, 6 added to list of valid entries

Thu Nov 25 19:15:11 PST 2021
Valid word entry ship, 9 added to list of valid entries

Thu Nov 25 19:15:11 PST 2021
Valid word entry zone, 13 added to list of valid entries

Thu Nov 25 19:15:11 PST 2021
Valid word entry bite, 6 added to list of valid entries