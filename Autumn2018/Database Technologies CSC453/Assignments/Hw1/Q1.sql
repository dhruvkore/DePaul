CREATE TABLE players(
    playerId number(5),
    name varchar(40),
    position varchar(40),
    skillLevel number(5),
    PRIMARY KEY (playerId)
);

CREATE TABLE teams(
    teamId number(5),
    name varchar(40),
    city varchar(40),
    coach varchar(40),
    captain number(5) UNIQUE,
    PRIMARY KEY (teamId),
    CONSTRAINT FK_captain FOREIGN KEY(captain) REFERENCES players(playerId)
);

CREATE TABLE teamToPlayers(
    teamId number(5),
    playerId number(5) UNIQUE,
    CONSTRAINT FK_playerId FOREIGN KEY(playerId) REFERENCES players(playerId),
    CONSTRAINT FK_teamId FOREIGN KEY(teamId) REFERENCES teams(teamId),
    PRIMARY KEY (teamId, playerId)
    );

CREATE TABLE playerToInjury(
    playerId number(5),
    Injury varchar(40),
    CONSTRAINT FK_playerIdtoinjury FOREIGN KEY(playerId) REFERENCES players(playerId)
);

CREATE TABLE games(
    gamedate DATE,
    hostTeamId number(5),
    guestTeamId number(5),
    hostScore number(5),
    guestScore number(5),
    CONSTRAINT FK_hostteamId FOREIGN KEY(hostTeamId) REFERENCES teams(teamId),
    CONSTRAINT FK_guestteamId FOREIGN KEY(guestTeamId) REFERENCES teams(teamId)
);



INSERT INTO players VALUES(1, 'First Captain', 'QB', 24);
INSERT INTO players VALUES(2, 'Second Captain', 'QB', 50);
INSERT INTO players VALUES(3, 'Third Captain', 'QB', 4);

INSERT INTO teams VALUES(4, 'BEARS', 'Chicago', 'SomeGuy Coach', 1);
INSERT INTO teams VALUES(5, 'PACKERS', 'Green Bay', 'OtherGuy Coach', 2);
INSERT INTO teams VALUES(8, 'NoTeam', 'Madeup', 'ThirdGuy Coach', 3);

INSERT INTO teamToPlayers VALUES(4, 1);
INSERT INTO teamToPlayers VALUES(5, 2);
INSERT INTO teamToPlayers VALUES(8, 3);

INSERT INTO playerToInjury VALUES(1, 'Dislocated ear');
INSERT INTO playerToInjury VALUES(1, 'Dislocated other ear');

INSERT INTO games VALUES(to_date('2018-12-10', 'yyyy-mm-dd'), 4, 5, 10, 14);
INSERT INTO games VALUES(to_date('2019-12-10', 'yyyy-mm-dd'), 4, 8, 21, 14);


