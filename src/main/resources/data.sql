TRUNCATE TABLE availability_slots, doctors, users, departments RESTART IDENTITY CASCADE;

INSERT INTO departments (name, description) VALUES
                                                ('Cardiology', 'Heart and cardiovascular diseases'),
                                                ('Pediatrics', 'Children healthcare and diseases'),
                                                ('Neurology', 'Brain and nervous system treatment'),
                                                ('Orthopedics', 'Bones and joint surgeries');

INSERT INTO users (username, password_hash, phone, role, status, created_at) VALUES
                                                                                 ('dr_ahmed', '$2a$10$hcoYBHwa3UodM3CwCeM7IeqFChodfYV1h3DdfwopP/ogQxjxUlUvq', '01011112222', 'DOCTOR', 'ACTIVE', CURRENT_TIMESTAMP),
                                                                                 ('dr_sara',  '$2a$10$hUSpHgbADEMnj/FxoTARt.L1.SpxLmUWyXe4IoDRrZWEAfHu1c9ey', '01022223333', 'DOCTOR', 'ACTIVE', CURRENT_TIMESTAMP),
                                                                                 ('dr_mora',  '$2a$10$TeepqLLnhHx/DUS9lOtEm.WVy4hSxJiE/bx4JL0uBg5VkvlmPUyb2', '01033334444', 'DOCTOR', 'ACTIVE', CURRENT_TIMESTAMP),
                                                                                 ('dr_omar',  '$2a$10$xBHMaVymr/srB45px85xgOIHbUxnA.qTxjR7/QEUhTt5rm7phdbTC', '01044445555', 'DOCTOR', 'ACTIVE', CURRENT_TIMESTAMP);

INSERT INTO doctors (user_id, department_id, first_name, last_name, specialization) VALUES
                                                                                        (1, 1, 'Ahmed', 'Abdelhalem', 'Interventional Cardiologist'),
                                                                                        (2, 1, 'Sara', 'Mahmoud', 'General Cardiologist'),
                                                                                        (3, 2, 'Mora', 'Ibrahim', 'Pediatric Specialist'),
                                                                                        (4, 3, 'Omar', 'Hassan', 'Neurologist');

INSERT INTO availability_slots (doctor_id, date, start_time, end_time, status) VALUES
                                                                                   (1, '2026-08-30', '09:00:00', '09:30:00', 'AVAILABLE'),
                                                                                   (1, '2026-08-30', '09:30:00', '10:00:00', 'AVAILABLE'),
                                                                                   (1, '2026-08-30', '10:00:00', '10:30:00', 'BOOKED'),
                                                                                   (2, '2026-08-30', '11:00:00', '11:30:00', 'AVAILABLE'),
                                                                                   (2, '2026-08-31', '12:00:00', '12:30:00', 'AVAILABLE'),
                                                                                   (3, '2026-08-30', '14:00:00', '14:30:00', 'AVAILABLE'),
                                                                                   (3, '2026-08-30', '14:30:00', '15:00:00', 'AVAILABLE'),
                                                                                   (4, '2026-08-30', '16:00:00', '16:30:00', 'BOOKED');

INSERT INTO users (username, password_hash, phone, role, status, created_at) VALUES
    ('patient_mostafa', '$2a$10$IkhF4AEfmsXNHsQDTBzzMOjBklN0iRy3TEkunIT8qWhM8awJBVuPm', '01055556666', 'PATIENT', 'ACTIVE', CURRENT_TIMESTAMP);

INSERT INTO patients (user_id, first_name, last_name, date_of_birth, gender, created_at) VALUES
    (5, 'Mostafa', 'Kharbita', '2005-01-01', 'MALE', CURRENT_TIMESTAMP);