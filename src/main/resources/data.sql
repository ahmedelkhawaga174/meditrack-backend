-- MediTrack development seed data
-- IMPORTANT: use this on a clean development database.
-- Seeded password for every account: pass123
-- Test date: 2026-09-15
-- Patient 1 (Mostafa Kharbita) has both past and upcoming appointments.

INSERT INTO departments (id, name, description) VALUES
                                                    (1, 'Cardiology', 'Heart and cardiovascular care'),
                                                    (2, 'Dermatology', 'Skin and hair care'),
                                                    (3, 'Neurology', 'Brain and nervous system care'),
                                                    (4, 'Pediatrics', 'Medical care for children'),
                                                    (5, 'General Medicine', 'General medical consultations'),
                                                    (6, 'Orthopedics', 'Bones and joint care'),
                                                    (7, 'Ophthalmology', 'Eye care'),
                                                    (8, 'Dentistry', 'Dental care'),
                                                    (9, 'Psychiatry', 'Mental health care'),
                                                    (10, 'Gastroenterology', 'Digestive system care');

INSERT INTO users (id, username, password_hash, phone, role, status, created_at) VALUES
                                                                                     (1, 'patient1', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', '01055556666', 'PATIENT', 'ACTIVE', CURRENT_TIMESTAMP),
                                                                                     (2, 'patient2', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', '01055556667', 'PATIENT', 'ACTIVE', CURRENT_TIMESTAMP),
                                                                                     (3, 'patient3', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', '01055556668', 'PATIENT', 'ACTIVE', CURRENT_TIMESTAMP),
                                                                                     (4, 'patient4', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', '01055556669', 'PATIENT', 'ACTIVE', CURRENT_TIMESTAMP),
                                                                                     (5, 'patient5', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', '01055556670', 'PATIENT', 'ACTIVE', CURRENT_TIMESTAMP),
                                                                                     (11, 'doctor1', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', '01110000001', 'DOCTOR', 'ACTIVE', CURRENT_TIMESTAMP),
                                                                                     (12, 'doctor2', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', '01110000002', 'DOCTOR', 'ACTIVE', CURRENT_TIMESTAMP),
                                                                                     (13, 'doctor3', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', '01110000003', 'DOCTOR', 'ACTIVE', CURRENT_TIMESTAMP),
                                                                                     (14, 'doctor4', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', '01110000004', 'DOCTOR', 'ACTIVE', CURRENT_TIMESTAMP),
                                                                                     (15, 'doctor5', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', '01110000005', 'DOCTOR', 'ACTIVE', CURRENT_TIMESTAMP),
                                                                                     (16, 'doctor6', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', '01110000006', 'DOCTOR', 'ACTIVE', CURRENT_TIMESTAMP),
                                                                                     (17, 'doctor7', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', '01110000007', 'DOCTOR', 'ACTIVE', CURRENT_TIMESTAMP),
                                                                                     (18, 'doctor8', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', '01110000008', 'DOCTOR', 'ACTIVE', CURRENT_TIMESTAMP),
                                                                                     (19, 'doctor9', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', '01110000009', 'DOCTOR', 'ACTIVE', CURRENT_TIMESTAMP),
                                                                                     (20, 'doctor10', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', '01110000010', 'DOCTOR', 'ACTIVE', CURRENT_TIMESTAMP);

INSERT INTO patients (id, user_id, first_name, last_name, date_of_birth, gender, created_at) VALUES
                                                                                                 (1, 1, 'Mostafa', 'Kharbita', '2005-01-01', 'MALE', CURRENT_TIMESTAMP),
                                                                                                 (2, 2, 'Ahmed', 'Hassan', '1995-01-15', 'MALE', CURRENT_TIMESTAMP),
                                                                                                 (3, 3, 'Omar', 'Ali', '1992-03-20', 'MALE', CURRENT_TIMESTAMP),
                                                                                                 (4, 4, 'Sara', 'Ahmed', '1994-02-18', 'FEMALE', CURRENT_TIMESTAMP),
                                                                                                 (5, 5, 'Mariam', 'Hassan', '1999-04-22', 'FEMALE', CURRENT_TIMESTAMP);

INSERT INTO doctors (id, user_id, department_id, first_name, last_name, specialization) VALUES
                                                                                            (1, 11, 1, 'Ahmed', 'El-Sayed', 'Cardiologist'),
                                                                                            (2, 12, 1, 'Sara', 'Mahmoud', 'General Cardiologist'),
                                                                                            (3, 13, 4, 'Mora', 'Ibrahim', 'Pediatric Specialist'),
                                                                                            (4, 14, 3, 'Omar', 'Hassan', 'Neurologist'),
                                                                                            (5, 15, 6, 'Mostafa', 'Khaled', 'Orthopedic Surgeon'),
                                                                                            (6, 16, 2, 'Nour', 'Samir', 'Dermatologist'),
                                                                                            (7, 17, 7, 'Youssef', 'Adel', 'Ophthalmologist'),
                                                                                            (8, 18, 8, 'Laila', 'Tarek', 'Dentist'),
                                                                                            (9, 19, 9, 'Karim', 'Wael', 'Psychiatrist'),
                                                                                            (10, 20, 10, 'Hana', 'Ibrahim', 'Gastroenterologist');

INSERT INTO availability_slots (id, doctor_id, date, start_time, end_time, status) VALUES
                                                                                       (1, 1, '2026-09-10', '09:00:00', '09:30:00', 'AVAILABLE'),
                                                                                       (2, 1, '2026-09-15', '11:00:00', '11:30:00', 'AVAILABLE'),
                                                                                       (3, 1, '2026-09-16', '09:00:00', '09:30:00', 'AVAILABLE'),
                                                                                       (4, 1, '2026-09-17', '14:00:00', '14:30:00', 'AVAILABLE'),
                                                                                       (5, 2, '2026-09-10', '09:00:00', '09:30:00', 'AVAILABLE'),
                                                                                       (6, 2, '2026-09-15', '11:00:00', '11:30:00', 'AVAILABLE'),
                                                                                       (7, 2, '2026-09-16', '09:00:00', '09:30:00', 'AVAILABLE'),
                                                                                       (8, 2, '2026-09-17', '14:00:00', '14:30:00', 'AVAILABLE'),
                                                                                       (9, 3, '2026-09-10', '09:00:00', '09:30:00', 'AVAILABLE'),
                                                                                       (10, 3, '2026-09-15', '11:00:00', '11:30:00', 'AVAILABLE'),
                                                                                       (11, 3, '2026-09-16', '09:00:00', '09:30:00', 'AVAILABLE'),
                                                                                       (12, 3, '2026-09-17', '14:00:00', '14:30:00', 'AVAILABLE'),
                                                                                       (13, 4, '2026-09-10', '09:00:00', '09:30:00', 'AVAILABLE'),
                                                                                       (14, 4, '2026-09-15', '11:00:00', '11:30:00', 'AVAILABLE'),
                                                                                       (15, 4, '2026-09-16', '09:00:00', '09:30:00', 'AVAILABLE'),
                                                                                       (16, 4, '2026-09-17', '14:00:00', '14:30:00', 'AVAILABLE'),
                                                                                       (17, 5, '2026-09-10', '09:00:00', '09:30:00', 'AVAILABLE'),
                                                                                       (18, 5, '2026-09-15', '11:00:00', '11:30:00', 'AVAILABLE'),
                                                                                       (19, 5, '2026-09-16', '09:00:00', '09:30:00', 'AVAILABLE'),
                                                                                       (20, 5, '2026-09-17', '14:00:00', '14:30:00', 'AVAILABLE'),
                                                                                       (21, 6, '2026-09-10', '09:00:00', '09:30:00', 'AVAILABLE'),
                                                                                       (22, 6, '2026-09-15', '11:00:00', '11:30:00', 'AVAILABLE'),
                                                                                       (23, 6, '2026-09-16', '09:00:00', '09:30:00', 'AVAILABLE'),
                                                                                       (24, 6, '2026-09-17', '14:00:00', '14:30:00', 'AVAILABLE'),
                                                                                       (25, 7, '2026-09-10', '09:00:00', '09:30:00', 'AVAILABLE'),
                                                                                       (26, 7, '2026-09-15', '11:00:00', '11:30:00', 'AVAILABLE'),
                                                                                       (27, 7, '2026-09-16', '09:00:00', '09:30:00', 'AVAILABLE'),
                                                                                       (28, 7, '2026-09-17', '14:00:00', '14:30:00', 'AVAILABLE'),
                                                                                       (29, 8, '2026-09-10', '09:00:00', '09:30:00', 'AVAILABLE'),
                                                                                       (30, 8, '2026-09-15', '11:00:00', '11:30:00', 'AVAILABLE'),
                                                                                       (31, 8, '2026-09-16', '09:00:00', '09:30:00', 'AVAILABLE'),
                                                                                       (32, 8, '2026-09-17', '14:00:00', '14:30:00', 'AVAILABLE'),
                                                                                       (33, 9, '2026-09-10', '09:00:00', '09:30:00', 'AVAILABLE'),
                                                                                       (34, 9, '2026-09-15', '11:00:00', '11:30:00', 'AVAILABLE'),
                                                                                       (35, 9, '2026-09-16', '09:00:00', '09:30:00', 'AVAILABLE'),
                                                                                       (36, 9, '2026-09-17', '14:00:00', '14:30:00', 'AVAILABLE'),
                                                                                       (37, 10, '2026-09-10', '09:00:00', '09:30:00', 'AVAILABLE'),
                                                                                       (38, 10, '2026-09-15', '11:00:00', '11:30:00', 'AVAILABLE'),
                                                                                       (39, 10, '2026-09-16', '09:00:00', '09:30:00', 'AVAILABLE'),
                                                                                       (40, 10, '2026-09-17', '14:00:00', '14:30:00', 'AVAILABLE');

INSERT INTO appointments (id, patient_id, doctor_id, slot_id, status, created_at, notes) VALUES
                                                                                             (1,1,1,1,'COMPLETED',CURRENT_TIMESTAMP,'Cardiology follow-up'),
                                                                                             (2,1,1,2,'CONFIRMED',CURRENT_TIMESTAMP,'Routine cardiology consultation'),
                                                                                             (3,2,2,6,'CONFIRMED',CURRENT_TIMESTAMP,'Cardiology consultation'),
                                                                                             (4,3,3,10,'CONFIRMED',CURRENT_TIMESTAMP,'Pediatric consultation'),
                                                                                             (5,1,4,13,'CONFIRMED',CURRENT_TIMESTAMP,'Neurology consultation'),
                                                                                             (6,4,5,17,'CONFIRMED',CURRENT_TIMESTAMP,'General checkup');

UPDATE availability_slots SET status = 'BOOKED' WHERE id IN (1,2,6,10,13,17);

INSERT INTO referrals (id, appointment_id, referred_to_doctor_id, referral_reason, status, created_at) VALUES
                                                                                                           (1,1,2,'Needs cardiology specialist follow-up.','PENDING',CURRENT_TIMESTAMP),
                                                                                                           (2,3,4,'Needs neurological evaluation.','PENDING',CURRENT_TIMESTAMP),
                                                                                                           (3,4,5,'Needs specialist follow-up.','PENDING',CURRENT_TIMESTAMP);