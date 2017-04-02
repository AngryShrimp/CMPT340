% Facts: Courses
course(cmpt214).
course(cmpt270).
course(cmpt260).
course(cmpt280).
course(cmpt215).
course(cmpt340).
course(cmpt360).
course(cmpt332).

% Facts: Professors
professor(kusalik).
professor(mcquillan).
professor(stavness).
professor(keil).
professor(eramian).
professor(dutchyn).
professor(jamali).
professor(makaroff).

% Facts: Who teaches each course?
professorOf(cmpt214,kusalik).
professorOf(cmpt270,mcquillan).
professorOf(cmpt270,stavness).
professorOf(cmpt260,keil).
professorOf(cmpt280,eramian).
professorOf(cmpt215,dutchyn).
professorOf(cmpt340,jamali).
professorOf(cmpt360,keil).
professorOf(cmpt332,makaroff).

% Facts: What are the pre-requisites of each course?
prereqOf(cmpt340,cmpt214).
prereqOf(cmpt340,cmpt270).
prereqOf(cmpt340,cmpt260).
prereqOf(cmpt360,cmpt260).
prereqOf(cmpt360,cmpt280).
prereqOf(cmpt332,cmpt215).