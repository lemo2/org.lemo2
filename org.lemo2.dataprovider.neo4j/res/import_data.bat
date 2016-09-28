SET DATABASE_PATH=C:/Users/fschwank/Documents/Neo4j/LEMO_Test2
SET NEO4J_BIN=C:/neo4j-community-3.0.1/bin/

CALL %NEO4J_BIN%neo4j-import.bat --into %DATABASE_PATH% --id-type string --skip-duplicate-nodes true --bad-tolerance 50000 ^
--nodes:LearningActivity "./import_d4la/Nodes/d4la_activity.csv" ^
--nodes:LearningContext "./import_d4la/Nodes/d4la_context.csv" ^
--nodes:LearningObject "./import_d4la/Nodes/d4la_object.csv" ^
--nodes:Person "./import_d4la/Nodes/d4la_person.csv" ^
--relationships:REFERENCES "./import_d4la/Relationships/d4la_activity_activity.csv" ^
--relationships:HAS_ACTIVITY "./import_d4la/Relationships/d4la_context_activity.csv" ^
--relationships:USES "./import_d4la/Relationships/d4la_object_activity.csv" ^
--relationships:DOES "./import_d4la/Relationships/d4la_person_activity.csv" ^
--relationships:HAS_OBJECT "./import_d4la/Relationships/d4la_object_context.csv" ^
--relationships:PARTICIPATES_IN "./import_d4la/Relationships/d4la_person_context.csv" ^
--relationships:HAS_PERSON "./import_d4la/Relationships/d4la_context_person.csv" ^
--relationships:PARENT_OF "./import_d4la/Relationships/d4la_context_context.csv" ^
--relationships:PARENT_OF "./import_d4la/Relationships/d4la_object_object.csv"

CALL %NEO4J_BIN%neo4j-shell.bat -path %DATABASE_PATH% -c ^
CREATE CONSTRAINT ON (n:LearningActivity) ASSERT n.activityID IS UNIQUE; ^
CREATE CONSTRAINT ON (n:LearningContext) ASSERT n.contextID IS UNIQUE; ^
CREATE CONSTRAINT ON (n:LearningObject) ASSERT n.objectID IS UNIQUE; ^
CREATE CONSTRAINT ON (n:Person) ASSERT n.personID IS UNIQUE; ^
CREATE INDEX ON :LearningContext(id); ^
CREATE INDEX ON :LearningObject(id); ^
CREATE INDEX ON :LearningActivity(id); ^
CREATE INDEX ON :Person(id); ^
CREATE INDEX ON :Answer(activityID); ^
CREATE INDEX ON :Answer_update(activityID); ^
CREATE INDEX ON :Comment(activityID); ^
CREATE INDEX ON :Pause(activityID); ^
CREATE INDEX ON :Play(activityID); ^
CREATE INDEX ON :Question(activityID); ^
CREATE INDEX ON :Record_progress(activityID); ^
CREATE INDEX ON :Stop(activityID); ^
CREATE INDEX ON :View(activityID); ^
CREATE INDEX ON :Vote_down(activityID); ^
CREATE INDEX ON :Vote_up(activityID); ^
CREATE INDEX ON :Progress(objectID); ^
CREATE INDEX ON :Questions(objectID); ^
CREATE INDEX ON :Segment(objectID); ^
CREATE INDEX ON :Video(objectID);