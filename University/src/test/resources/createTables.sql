DROP TABLE IF EXISTS university.groups CASCADE;
DROP TABLE IF EXISTS university.students CASCADE;
DROP TABLE IF EXISTS university.teachers CASCADE;
DROP TABLE IF EXISTS university.rooms CASCADE;
DROP TABLE IF EXISTS university.lessonsCASCADE;
DROP SCHEMA IF EXISTS university CASCADE;

CREATE SCHEMA IF NOT EXISTS university;

CREATE TABLE university.groups (
    group_id SERIAL,
    group_name VARCHAR NOT NULL,
    PRIMARY KEY (group_id) 
);

CREATE TABLE university.students(
    student_id SERIAL,
    first_name VARCHAR NOT NULL,
    last_name VARCHAR NOT NULL,
    group_id INTEGER,
    PRIMARY KEY (student_id),
    FOREIGN KEY (group_id) REFERENCES university.groups(group_id)
);

CREATE TABLE university.teachers(
    teacher_id SERIAL,
    first_name VARCHAR NOT NULL,
    last_name VARCHAR NOT NULL,
    PRIMARY KEY (teacher_id)
);

CREATE TABLE university.rooms(
    room_id SERIAL,
    room_number INTEGER NOT NULL,
    PRIMARY KEY (room_id)
);

CREATE TABLE university.lessons(
    lesson_id SERIAL,
    lesson_name VARCHAR NOT NULL,
    teacher_id INTEGER,
    group_id INTEGER,
    room_id INTEGER,
    lesson_date date,
    PRIMARY KEY (lesson_id),
    FOREIGN KEY (teacher_id) REFERENCES university.teachers(teacher_id),
    FOREIGN KEY (group_id) REFERENCES university.groups(group_id),
    FOREIGN KEY (room_id) REFERENCES university.rooms(room_id)
);