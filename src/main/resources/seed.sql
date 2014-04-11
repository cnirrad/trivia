INSERT INTO GAME (ID, CURRENT_QUESTION_IDX, NUM_SECONDS_BETWEEN_QUESTIONS, NUM_SECONDS_PER_QUESTION, STATE) VALUES
(1, 0, 10, 10, 'NOT_STARTED');

INSERT INTO QUESTION (ID, TEXT, OPTIONA, OPTIONB, OPTIONC, OPTIOND, CORRECT_ANSWER) VALUES
(1, 'How many bones is a baby born with?',
'200',
'206',
'250',
'300',
'D');

INSERT INTO QUESTION (ID, TEXT, OPTIONA, OPTIONB, OPTIONC, OPTIOND, CORRECT_ANSWER) VALUES
(2, 'Brian was born in which city?',
'Elkader',
'Prairie du Chien',
'West Union',
'Guttenburg',
'B');

INSERT INTO QUESTION (ID, TEXT, OPTIONA, OPTIONB, OPTIONC, OPTIOND, CORRECT_ANSWER) VALUES
(3, 'Which pop came first?',
'RC Cola',
'Pepsi',
'Coke',
'Dr. Pepper',
'D');

INSERT INTO QUESTION (ID, TEXT, OPTIONA, OPTIONB, OPTIONC, OPTIOND, CORRECT_ANSWER) VALUES
(4, 'Which city was Jodi born in?',
'Dubuque',
'Iowa City',
'Cascade',
'Manchester',
'A');

INSERT INTO QUESTION (ID, TEXT, OPTIONA, OPTIONB, OPTIONC, OPTIOND, CORRECT_ANSWER) VALUES
(5, 'Who was the original Gerber Baby?',
'Shirley Temple',
'Judy Garland',
'Ann Turner',
'Betty White',
'C');

INSERT INTO QUESTION (ID, TEXT, OPTIONA, OPTIONB, OPTIONC, OPTIOND, CORRECT_ANSWER) VALUES
(6, 'Until babies are 7 months old, what can they do that adults cannot?',
'cross their eyes while sticking out their tongue',
'lick their elbow',
'breathe and swallow simultaneously',
'sneeze with their eyes open',
'C');

INSERT INTO QUESTION (ID, TEXT, OPTIONA, OPTIONB, OPTIONC, OPTIOND, CORRECT_ANSWER) VALUES
(7, 'Brian raised pygmy goats. What did he name them after?',
'Greek God/Goddesses',
'Teenage Mutant Ninja Turtles',
'US Presidents',
'Star Wars characters',
'A');

INSERT INTO QUESTION (ID, TEXT, OPTIONA, OPTIONB, OPTIONC, OPTIOND, CORRECT_ANSWER) VALUES
(8, 'What was the occupation of the three different inventors of Coke, Pepsi, and Dr. Pepper?',
'Bartenders',
'Pharmacists',
'Restaurant Owners',
'Doctors',
'B');

INSERT INTO QUESTION (ID, TEXT, OPTIONA, OPTIONB, OPTIONC, OPTIOND, CORRECT_ANSWER) VALUES
(9, 'What is the name of the horse Jodi is picture with?',
'Midnight',
'Sonny',
'Robin',
'Horse',
'C');

INSERT INTO QUESTION (ID, TEXT, OPTIONA, OPTIONB, OPTIONC, OPTIOND, CORRECT_ANSWER) VALUES
(10, 'Which presidents wife had a baby while in office?',
'John F. Kennedy',
'Theodore Roosevelt',
'Franklin Pierce',
'Grover Cleveland',
'D');

INSERT INTO QUESTION (ID, TEXT, OPTIONA, OPTIONB, OPTIONC, OPTIOND, CORRECT_ANSWER) VALUES
(11, 'What is the most number of children born to one woman?',
'21',
'45',
'54',
'69',
'D');

INSERT INTO QUESTION (ID, TEXT, OPTIONA, OPTIONB, OPTIONC, OPTIOND, CORRECT_ANSWER) VALUES
(12, 'What instrument did Brian play?',
'Trumpet',
'Sax',
'Tuba',
'None of the above',
'D');

INSERT INTO QUESTION (ID, TEXT, OPTIONA, OPTIONB, OPTIONC, OPTIOND, CORRECT_ANSWER) VALUES
(13, 'What do the A & W in A & W Root Beer stand for?',
'Allen & Wright',
'Alabama & Wyoming',
'Anderson & West',
'Albert & Wesley',
'A');

INSERT INTO QUESTION (ID, TEXT, OPTIONA, OPTIONB, OPTIONC, OPTIOND, CORRECT_ANSWER) VALUES
(14, 'What instrument did Jodi play?',
'Flute',
'Clarinet',
'Piano',
'French Horn',
'C');

INSERT INTO QUESTION (ID, TEXT, OPTIONA, OPTIONB, OPTIONC, OPTIOND, CORRECT_ANSWER) VALUES
(15, 'Which US President signed orders to make Mothers Day a national holiday?',
'Woodrow Wilson',
'Warren Harding',
'Theodore Roosevelt',
'Calvin Coolidge',
'A');




INSERT INTO GAME_QUESTIONS (GAME_ID, QUESTIONS_ID) VALUES (1, 1);
INSERT INTO GAME_QUESTIONS (GAME_ID, QUESTIONS_ID) VALUES (1, 2);
INSERT INTO GAME_QUESTIONS (GAME_ID, QUESTIONS_ID) VALUES (1, 3);
INSERT INTO GAME_QUESTIONS (GAME_ID, QUESTIONS_ID) VALUES (1, 4);
INSERT INTO GAME_QUESTIONS (GAME_ID, QUESTIONS_ID) VALUES (1, 5);
INSERT INTO GAME_QUESTIONS (GAME_ID, QUESTIONS_ID) VALUES (1, 6);
INSERT INTO GAME_QUESTIONS (GAME_ID, QUESTIONS_ID) VALUES (1, 7);
INSERT INTO GAME_QUESTIONS (GAME_ID, QUESTIONS_ID) VALUES (1, 8);
INSERT INTO GAME_QUESTIONS (GAME_ID, QUESTIONS_ID) VALUES (1, 9);
INSERT INTO GAME_QUESTIONS (GAME_ID, QUESTIONS_ID) VALUES (1, 10);
INSERT INTO GAME_QUESTIONS (GAME_ID, QUESTIONS_ID) VALUES (1, 11);
INSERT INTO GAME_QUESTIONS (GAME_ID, QUESTIONS_ID) VALUES (1, 12);
INSERT INTO GAME_QUESTIONS (GAME_ID, QUESTIONS_ID) VALUES (1, 13);
INSERT INTO GAME_QUESTIONS (GAME_ID, QUESTIONS_ID) VALUES (1, 14);
INSERT INTO GAME_QUESTIONS (GAME_ID, QUESTIONS_ID) VALUES (1, 15);

