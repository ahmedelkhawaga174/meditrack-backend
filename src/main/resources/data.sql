-- -- ============================================================
-- -- MediTrack Development Seed Data
-- -- Release 1 - Patient Booking MVP
-- -- ============================================================
-- --
-- -- IMPORTANT:
-- -- This file is intentionally safe to run on application startup.
-- -- It does NOT truncate existing application data.
-- --
-- -- Development login password for seeded users:
-- -- pass123
-- --
-- -- Passwords below are BCrypt hashes for "pass123".
-- -- ============================================================
--
--
-- -- ============================================================
-- -- DEPARTMENTS
-- -- ============================================================
--
-- INSERT INTO departments (name, description)
-- SELECT 'Cardiology', 'Heart and cardiovascular diseases'
-- WHERE NOT EXISTS (
--     SELECT 1 FROM departments WHERE name = 'Cardiology'
-- );
--
-- INSERT INTO departments (name, description)
-- SELECT 'Pediatrics', 'Children healthcare and diseases'
-- WHERE NOT EXISTS (
--     SELECT 1 FROM departments WHERE name = 'Pediatrics'
-- );
--
-- INSERT INTO departments (name, description)
-- SELECT 'Neurology', 'Brain and nervous system treatment'
-- WHERE NOT EXISTS (
--     SELECT 1 FROM departments WHERE name = 'Neurology'
-- );
--
-- INSERT INTO departments (name, description)
-- SELECT 'Orthopedics', 'Bones and joint surgeries'
-- WHERE NOT EXISTS (
--     SELECT 1 FROM departments WHERE name = 'Orthopedics'
-- );
--
--
-- -- ============================================================
-- -- DOCTOR USERS
-- -- ============================================================
--
-- INSERT INTO users (
--     username,
--     password_hash,
--     phone,
--     role,
--     status,
--     created_at
-- )
-- SELECT
--     'dr_ahmed',
--     '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy',
--     '01011112222',
--     'DOCTOR',
--     'ACTIVE',
--     CURRENT_TIMESTAMP
-- WHERE NOT EXISTS (
--     SELECT 1 FROM users WHERE phone = '01011112222'
-- );
--
-- INSERT INTO users (
--     username,
--     password_hash,
--     phone,
--     role,
--     status,
--     created_at
-- )
-- SELECT
--     'dr_sara',
--     '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy',
--     '01022223333',
--     'DOCTOR',
--     'ACTIVE',
--     CURRENT_TIMESTAMP
-- WHERE NOT EXISTS (
--     SELECT 1 FROM users WHERE phone = '01022223333'
-- );
--
-- INSERT INTO users (
--     username,
--     password_hash,
--     phone,
--     role,
--     status,
--     created_at
-- )
-- SELECT
--     'dr_mora',
--     '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy',
--     '01033334444',
--     'DOCTOR',
--     'ACTIVE',
--     CURRENT_TIMESTAMP
-- WHERE NOT EXISTS (
--     SELECT 1 FROM users WHERE phone = '01033334444'
-- );
--
-- INSERT INTO users (
--     username,
--     password_hash,
--     phone,
--     role,
--     status,
--     created_at
-- )
-- SELECT
--     'dr_omar',
--     '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy',
--     '01044445555',
--     'DOCTOR',
--     'ACTIVE',
--     CURRENT_TIMESTAMP
-- WHERE NOT EXISTS (
--     SELECT 1 FROM users WHERE phone = '01044445555'
-- );
--
-- INSERT INTO users (
--     username,
--     password_hash,
--     phone,
--     role,
--     status,
--     created_at
-- )
-- SELECT
--     'dr_mostafa',
--     '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy',
--     '01066667777',
--     'DOCTOR',
--     'ACTIVE',
--     CURRENT_TIMESTAMP
-- WHERE NOT EXISTS (
--     SELECT 1 FROM users WHERE phone = '01066667777'
-- );
--
--
-- -- ============================================================
-- -- DOCTORS
-- -- ============================================================
--
-- INSERT INTO doctors (
--     user_id,
--     department_id,
--     first_name,
--     last_name,
--     specialization
-- )
-- SELECT
--     u.id,
--     d.id,
--     'Ahmed',
--     'Abdelhalem',
--     'Interventional Cardiologist'
-- FROM users u
-- JOIN departments d ON d.name = 'Cardiology'
-- WHERE u.phone = '01011112222'
--   AND NOT EXISTS (
--       SELECT 1 FROM doctors doc WHERE doc.user_id = u.id
--   );
--
-- INSERT INTO doctors (
--     user_id,
--     department_id,
--     first_name,
--     last_name,
--     specialization
-- )
-- SELECT
--     u.id,
--     d.id,
--     'Sara',
--     'Mahmoud',
--     'General Cardiologist'
-- FROM users u
-- JOIN departments d ON d.name = 'Cardiology'
-- WHERE u.phone = '01022223333'
--   AND NOT EXISTS (
--       SELECT 1 FROM doctors doc WHERE doc.user_id = u.id
--   );
--
-- INSERT INTO doctors (
--     user_id,
--     department_id,
--     first_name,
--     last_name,
--     specialization
-- )
-- SELECT
--     u.id,
--     d.id,
--     'Mora',
--     'Ibrahim',
--     'Pediatric Specialist'
-- FROM users u
-- JOIN departments d ON d.name = 'Pediatrics'
-- WHERE u.phone = '01033334444'
--   AND NOT EXISTS (
--       SELECT 1 FROM doctors doc WHERE doc.user_id = u.id
--   );
--
-- INSERT INTO doctors (
--     user_id,
--     department_id,
--     first_name,
--     last_name,
--     specialization
-- )
-- SELECT
--     u.id,
--     d.id,
--     'Omar',
--     'Hassan',
--     'Neurologist'
-- FROM users u
-- JOIN departments d ON d.name = 'Neurology'
-- WHERE u.phone = '01044445555'
--   AND NOT EXISTS (
--       SELECT 1 FROM doctors doc WHERE doc.user_id = u.id
--   );
--
-- INSERT INTO doctors (
--     user_id,
--     department_id,
--     first_name,
--     last_name,
--     specialization
-- )
-- SELECT
--     u.id,
--     d.id,
--     'Mostafa',
--     'Khaled',
--     'Pediatric Specialist'
-- FROM users u
-- JOIN departments d ON d.name = 'Pediatrics'
-- WHERE u.phone = '01066667777'
--   AND NOT EXISTS (
--       SELECT 1 FROM doctors doc WHERE doc.user_id = u.id
--   );
--
--
-- -- ============================================================
-- -- PATIENT USER
-- -- ============================================================
-- --
-- -- This seeded patient is ACTIVE because it represents an already
-- -- verified patient account.
-- --
-- -- New patients created through /api/auth/register remain INACTIVE
-- -- until OTP verification.
-- -- ============================================================
--
-- INSERT INTO users (
--     username,
--     password_hash,
--     phone,
--     role,
--     status,
--     created_at
-- )
-- SELECT
--     'patient_mostafa',
--     '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy',
--     '01055556666',
--     'PATIENT',
--     'ACTIVE',
--     CURRENT_TIMESTAMP
-- WHERE NOT EXISTS (
--     SELECT 1 FROM users WHERE phone = '01055556666'
-- );
--
--
-- -- ============================================================
-- -- PATIENT
-- -- ============================================================
--
-- INSERT INTO patients (
--     user_id,
--     first_name,
--     last_name,
--     date_of_birth,
--     gender,
--     created_at
-- )
-- SELECT
--     u.id,
--     'Mostafa',
--     'Kharbita',
--     '2005-01-01',
--     'MALE',
--     CURRENT_TIMESTAMP
-- FROM users u
-- WHERE u.phone = '01055556666'
--   AND NOT EXISTS (
--       SELECT 1 FROM patients p WHERE p.user_id = u.id
--   );
--
--
-- -- ============================================================
-- -- AVAILABILITY SLOTS
-- -- ============================================================
--
-- INSERT INTO availability_slots (
--     doctor_id,
--     date,
--     start_time,
--     end_time,
--     status
-- )
-- SELECT
--     d.id,
--     '2026-09-15',
--     '09:00:00',
--     '09:30:00',
--     'AVAILABLE'
-- FROM doctors d
-- JOIN users u ON u.id = d.user_id
-- WHERE u.phone = '01011112222'
--   AND NOT EXISTS (
--       SELECT 1
--       FROM availability_slots s
--       WHERE s.doctor_id = d.id
--         AND s.date = '2026-09-15'
--         AND s.start_time = '09:00:00'
--   );
--
-- INSERT INTO availability_slots (
--     doctor_id,
--     date,
--     start_time,
--     end_time,
--     status
-- )
-- SELECT
--     d.id,
--     '2026-09-15',
--     '09:30:00',
--     '10:00:00',
--     'AVAILABLE'
-- FROM doctors d
-- JOIN users u ON u.id = d.user_id
-- WHERE u.phone = '01011112222'
--   AND NOT EXISTS (
--       SELECT 1
--       FROM availability_slots s
--       WHERE s.doctor_id = d.id
--         AND s.date = '2026-09-15'
--         AND s.start_time = '09:30:00'
--   );
--
-- INSERT INTO availability_slots (
--     doctor_id,
--     date,
--     start_time,
--     end_time,
--     status
-- )
-- SELECT
--     d.id,
--     '2026-09-15',
--     '10:00:00',
--     '10:30:00',
--     'BOOKED'
-- FROM doctors d
-- JOIN users u ON u.id = d.user_id
-- WHERE u.phone = '01011112222'
--   AND NOT EXISTS (
--       SELECT 1
--       FROM availability_slots s
--       WHERE s.doctor_id = d.id
--         AND s.date = '2026-09-15'
--         AND s.start_time = '10:00:00'
--   );
--
-- INSERT INTO availability_slots (
--     doctor_id,
--     date,
--     start_time,
--     end_time,
--     status
-- )
-- SELECT
--     d.id,
--     '2026-09-15',
--     '11:00:00',
--     '11:30:00',
--     'AVAILABLE'
-- FROM doctors d
-- JOIN users u ON u.id = d.user_id
-- WHERE u.phone = '01022223333'
--   AND NOT EXISTS (
--       SELECT 1
--       FROM availability_slots s
--       WHERE s.doctor_id = d.id
--         AND s.date = '2026-09-15'
--         AND s.start_time = '11:00:00'
--   );
--
-- INSERT INTO availability_slots (
--     doctor_id,
--     date,
--     start_time,
--     end_time,
--     status
-- )
-- SELECT
--     d.id,
--     '2026-09-15',
--     '12:00:00',
--     '12:30:00',
--     'AVAILABLE'
-- FROM doctors d
-- JOIN users u ON u.id = d.user_id
-- WHERE u.phone = '01022223333'
--   AND NOT EXISTS (
--       SELECT 1
--       FROM availability_slots s
--       WHERE s.doctor_id = d.id
--         AND s.date = '2026-09-15'
--         AND s.start_time = '12:00:00'
--   );
--
-- INSERT INTO availability_slots (
--     doctor_id,
--     date,
--     start_time,
--     end_time,
--     status
-- )
-- SELECT
--     d.id,
--     '2026-09-15',
--     '14:00:00',
--     '14:30:00',
--     'AVAILABLE'
-- FROM doctors d
-- JOIN users u ON u.id = d.user_id
-- WHERE u.phone = '01033334444'
--   AND NOT EXISTS (
--       SELECT 1
--       FROM availability_slots s
--       WHERE s.doctor_id = d.id
--         AND s.date = '2026-09-15'
--         AND s.start_time = '14:00:00'
--   );
--
-- INSERT INTO availability_slots (
--     doctor_id,
--     date,
--     start_time,
--     end_time,
--     status
-- )
-- SELECT
--     d.id,
--     '2026-09-15',
--     '14:30:00',
--     '15:00:00',
--     'AVAILABLE'
-- FROM doctors d
-- JOIN users u ON u.id = d.user_id
-- WHERE u.phone = '01033334444'
--   AND NOT EXISTS (
--       SELECT 1
--       FROM availability_slots s
--       WHERE s.doctor_id = d.id
--         AND s.date = '2026-09-15'
--         AND s.start_time = '14:30:00'
--   );
--
-- INSERT INTO availability_slots (
--     doctor_id,
--     date,
--     start_time,
--     end_time,
--     status
-- )
-- SELECT
--     d.id,
--     '2026-09-15',
--     '16:00:00',
--     '16:30:00',
--     'BOOKED'
-- FROM doctors d
-- JOIN users u ON u.id = d.user_id
-- WHERE u.phone = '01044445555'
--   AND NOT EXISTS (
--       SELECT 1
--       FROM availability_slots s
--       WHERE s.doctor_id = d.id
--         AND s.date = '2026-09-15'
--         AND s.start_time = '16:00:00'
--   );
-- -- ============================================================
-- -- APPOINTMENTS
-- -- ============================================================
--
-- INSERT INTO appointments (
--     patient_id,
--     doctor_id,
--     slot_id,
--     status,
--     notes,
--     created_at
-- )
-- SELECT
--     p.id,
--     d.id,
--     s.id,
--     'PENDING',
--     'Patient requires urgent cardiac consultation.',
--     CURRENT_TIMESTAMP
-- FROM patients p
--          JOIN users pu
--               ON pu.id = p.user_id
--          JOIN doctors d
--               ON d.id = (
--                   SELECT doc.id
--                   FROM doctors doc
--                            JOIN users du ON du.id = doc.user_id
--                   WHERE du.phone = '01011112222'
--               )
--          JOIN availability_slots s
--               ON s.doctor_id = d.id
-- WHERE pu.phone = '01055556666'
--   AND s.date = '2026-09-15'
--   AND s.start_time = '09:00:00'
--   AND NOT EXISTS (
--     SELECT 1
--     FROM appointments a
--     WHERE a.slot_id = s.id
-- );
--
-- INSERT INTO referrals (
--     appointment_id,
--     referred_to_doctor_id,
--     referral_reason,
--     status,
--     created_at
-- )
-- VALUES
--     (6, 2, 'Patient needs cardiology consultation.', 'PENDING', CURRENT_TIMESTAMP),
--     (2, 3, 'Patient requires neurological evaluation.', 'PENDING', CURRENT_TIMESTAMP),
--     (3, 2, 'Patient needs specialist follow-up.', 'PENDING', CURRENT_TIMESTAMP),
--     (8, 4, 'Patient requires additional medical assessment.', 'PENDING', CURRENT_TIMESTAMP),
--     (5, 3, 'Patient needs further diagnostic evaluation.', 'PENDING', CURRENT_TIMESTAMP);

-- **************************************************************************************************************

INSERT INTO departments (id, name, description)
VALUES
    (1, 'Cardiology', 'Heart and cardiovascular care'),
    (2, 'Dermatology', 'Skin and hair care'),
    (3, 'Neurology', 'Brain and nervous system care'),
    (4, 'Pediatrics', 'Medical care for children'),
    (5, 'General Medicine', 'General medical consultations'),
    (6, 'Orthopedics', 'Bones and joint care'),
    (7, 'Ophthalmology', 'Eye care'),
    (8, 'Dentistry', 'Dental care'),
    (9, 'Psychiatry', 'Mental health care'),
    (10, 'Gastroenterology', 'Digestive system care'),
    (11, 'Endocrinology', 'Hormonal disorders'),
    (12, 'Pulmonology', 'Respiratory system care'),
    (13, 'Urology', 'Urinary system care'),
    (14, 'Nephrology', 'Kidney care'),
    (15, 'Oncology', 'Cancer care'),
    (16, 'Radiology', 'Medical imaging'),
    (17, 'Surgery', 'Surgical procedures'),
    (18, 'Anesthesiology', 'Anesthesia and pain management'),
    (19, 'Rheumatology', 'Autoimmune and joint diseases'),
    (20, 'Hematology', 'Blood disorders'),
    (21, 'Infectious Diseases', 'Infection treatment'),
    (22, 'Allergy and Immunology', 'Allergy and immune disorders'),
    (23, 'Plastic Surgery', 'Reconstructive surgery'),
    (24, 'Vascular Surgery', 'Blood vessel surgery'),
    (25, 'Emergency Medicine', 'Emergency medical care'),
    (26, 'Family Medicine', 'Family healthcare'),
    (27, 'Obstetrics', 'Pregnancy and childbirth care'),
    (28, 'Gynecology', 'Women healthcare'),
    (29, 'Otolaryngology', 'Ear, nose and throat care'),
    (30, 'Clinical Pathology', 'Laboratory diagnosis');

INSERT INTO users (
    id,
    username,
    password_hash,
    phone,
    role,
    status,
    created_at
)
VALUES
    (1, 'patient1', 'password_hash_1', '01000000001', 'PATIENT', 'ACTIVE', CURRENT_TIMESTAMP),
    (2, 'patient2', 'password_hash_2', '01000000002', 'PATIENT', 'ACTIVE', CURRENT_TIMESTAMP),
    (3, 'patient3', 'password_hash_3', '01000000003', 'PATIENT', 'ACTIVE', CURRENT_TIMESTAMP),
    (4, 'patient4', 'password_hash_4', '01000000004', 'PATIENT', 'ACTIVE', CURRENT_TIMESTAMP),
    (5, 'patient5', 'password_hash_5', '01000000005', 'PATIENT', 'ACTIVE', CURRENT_TIMESTAMP),
    (6, 'patient6', 'password_hash_6', '01000000006', 'PATIENT', 'ACTIVE', CURRENT_TIMESTAMP),
    (7, 'patient7', 'password_hash_7', '01000000007', 'PATIENT', 'ACTIVE', CURRENT_TIMESTAMP),
    (8, 'patient8', 'password_hash_8', '01000000008', 'PATIENT', 'ACTIVE', CURRENT_TIMESTAMP),
    (9, 'patient9', 'password_hash_9', '01000000009', 'PATIENT', 'ACTIVE', CURRENT_TIMESTAMP),
    (10, 'patient10', 'password_hash_10', '01000000010', 'PATIENT', 'ACTIVE', CURRENT_TIMESTAMP),
    (11, 'patient11', 'password_hash_11', '01000000011', 'PATIENT', 'ACTIVE', CURRENT_TIMESTAMP),
    (12, 'patient12', 'password_hash_12', '01000000012', 'PATIENT', 'ACTIVE', CURRENT_TIMESTAMP),
    (13, 'patient13', 'password_hash_13', '01000000013', 'PATIENT', 'ACTIVE', CURRENT_TIMESTAMP),
    (14, 'patient14', 'password_hash_14', '01000000014', 'PATIENT', 'ACTIVE', CURRENT_TIMESTAMP),
    (15, 'patient15', 'password_hash_15', '01000000015', 'PATIENT', 'ACTIVE', CURRENT_TIMESTAMP),
    (16, 'patient16', 'password_hash_16', '01000000016', 'PATIENT', 'ACTIVE', CURRENT_TIMESTAMP),
    (17, 'patient17', 'password_hash_17', '01000000017', 'PATIENT', 'ACTIVE', CURRENT_TIMESTAMP),
    (18, 'patient18', 'password_hash_18', '01000000018', 'PATIENT', 'ACTIVE', CURRENT_TIMESTAMP),
    (19, 'patient19', 'password_hash_19', '01000000019', 'PATIENT', 'ACTIVE', CURRENT_TIMESTAMP),
    (20, 'patient20', 'password_hash_20', '01000000020', 'PATIENT', 'ACTIVE', CURRENT_TIMESTAMP),
    (21, 'patient21', 'password_hash_21', '01000000021', 'PATIENT', 'ACTIVE', CURRENT_TIMESTAMP),
    (22, 'patient22', 'password_hash_22', '01000000022', 'PATIENT', 'ACTIVE', CURRENT_TIMESTAMP),
    (23, 'patient23', 'password_hash_23', '01000000023', 'PATIENT', 'ACTIVE', CURRENT_TIMESTAMP),
    (24, 'patient24', 'password_hash_24', '01000000024', 'PATIENT', 'ACTIVE', CURRENT_TIMESTAMP),
    (25, 'patient25', 'password_hash_25', '01000000025', 'PATIENT', 'ACTIVE', CURRENT_TIMESTAMP),
    (26, 'patient26', 'password_hash_26', '01000000026', 'PATIENT', 'ACTIVE', CURRENT_TIMESTAMP),
    (27, 'patient27', 'password_hash_27', '01000000027', 'PATIENT', 'ACTIVE', CURRENT_TIMESTAMP),
    (28, 'patient28', 'password_hash_28', '01000000028', 'PATIENT', 'ACTIVE', CURRENT_TIMESTAMP),
    (29, 'patient29', 'password_hash_29', '01000000029', 'PATIENT', 'ACTIVE', CURRENT_TIMESTAMP),
    (30, 'patient30', 'password_hash_30', '01000000030', 'PATIENT', 'ACTIVE', CURRENT_TIMESTAMP),

    (31, 'doctor1', 'password_hash_31', '01100000001', 'DOCTOR', 'ACTIVE', CURRENT_TIMESTAMP),
    (32, 'doctor2', 'password_hash_32', '01100000002', 'DOCTOR', 'ACTIVE', CURRENT_TIMESTAMP),
    (33, 'doctor3', 'password_hash_33', '01100000003', 'DOCTOR', 'ACTIVE', CURRENT_TIMESTAMP),
    (34, 'doctor4', 'password_hash_34', '01100000004', 'DOCTOR', 'ACTIVE', CURRENT_TIMESTAMP),
    (35, 'doctor5', 'password_hash_35', '01100000005', 'DOCTOR', 'ACTIVE', CURRENT_TIMESTAMP),
    (36, 'doctor6', 'password_hash_36', '01100000006', 'DOCTOR', 'ACTIVE', CURRENT_TIMESTAMP),
    (37, 'doctor7', 'password_hash_37', '01100000007', 'DOCTOR', 'ACTIVE', CURRENT_TIMESTAMP),
    (38, 'doctor8', 'password_hash_38', '01100000008', 'DOCTOR', 'ACTIVE', CURRENT_TIMESTAMP),
    (39, 'doctor9', 'password_hash_39', '01100000009', 'DOCTOR', 'ACTIVE', CURRENT_TIMESTAMP),
    (40, 'doctor10', 'password_hash_40', '01100000010', 'DOCTOR', 'ACTIVE', CURRENT_TIMESTAMP),
    (41, 'doctor11', 'password_hash_41', '01100000011', 'DOCTOR', 'ACTIVE', CURRENT_TIMESTAMP),
    (42, 'doctor12', 'password_hash_42', '01100000012', 'DOCTOR', 'ACTIVE', CURRENT_TIMESTAMP),
    (43, 'doctor13', 'password_hash_43', '01100000013', 'DOCTOR', 'ACTIVE', CURRENT_TIMESTAMP),
    (44, 'doctor14', 'password_hash_44', '01100000014', 'DOCTOR', 'ACTIVE', CURRENT_TIMESTAMP),
    (45, 'doctor15', 'password_hash_45', '01100000015', 'DOCTOR', 'ACTIVE', CURRENT_TIMESTAMP),
    (46, 'doctor16', 'password_hash_46', '01100000016', 'DOCTOR', 'ACTIVE', CURRENT_TIMESTAMP),
    (47, 'doctor17', 'password_hash_47', '01100000017', 'DOCTOR', 'ACTIVE', CURRENT_TIMESTAMP),
    (48, 'doctor18', 'password_hash_48', '01100000018', 'DOCTOR', 'ACTIVE', CURRENT_TIMESTAMP),
    (49, 'doctor19', 'password_hash_49', '01100000019', 'DOCTOR', 'ACTIVE', CURRENT_TIMESTAMP),
    (50, 'doctor20', 'password_hash_50', '01100000020', 'DOCTOR', 'ACTIVE', CURRENT_TIMESTAMP),
    (51, 'doctor21', 'password_hash_51', '01100000021', 'DOCTOR', 'ACTIVE', CURRENT_TIMESTAMP),
    (52, 'doctor22', 'password_hash_52', '01100000022', 'DOCTOR', 'ACTIVE', CURRENT_TIMESTAMP),
    (53, 'doctor23', 'password_hash_53', '01100000023', 'DOCTOR', 'ACTIVE', CURRENT_TIMESTAMP),
    (54, 'doctor24', 'password_hash_54', '01100000024', 'DOCTOR', 'ACTIVE', CURRENT_TIMESTAMP),
    (55, 'doctor25', 'password_hash_55', '01100000025', 'DOCTOR', 'ACTIVE', CURRENT_TIMESTAMP),
    (56, 'doctor26', 'password_hash_56', '01100000026', 'DOCTOR', 'ACTIVE', CURRENT_TIMESTAMP),
    (57, 'doctor27', 'password_hash_57', '01100000027', 'DOCTOR', 'ACTIVE', CURRENT_TIMESTAMP),
    (58, 'doctor28', 'password_hash_58', '01100000028', 'DOCTOR', 'ACTIVE', CURRENT_TIMESTAMP),
    (59, 'doctor29', 'password_hash_59', '01100000029', 'DOCTOR', 'ACTIVE', CURRENT_TIMESTAMP),
    (60, 'doctor30', 'password_hash_60', '01100000030', 'DOCTOR', 'ACTIVE', CURRENT_TIMESTAMP);

INSERT INTO patients (
    id,
    user_id,
    first_name,
    last_name,
    date_of_birth,
    gender,
    created_at
)
VALUES
    (1, 1, 'Ahmed', 'Hassan', '1995-01-15', 'MALE', CURRENT_TIMESTAMP),
    (2, 2, 'Omar', 'Ali', '1992-03-20', 'MALE', CURRENT_TIMESTAMP),
    (3, 3, 'Mohamed', 'Ibrahim', '1998-05-10', 'MALE', CURRENT_TIMESTAMP),
    (4, 4, 'Youssef', 'Mahmoud', '1990-07-12', 'MALE', CURRENT_TIMESTAMP),
    (5, 5, 'Khaled', 'Samir', '1996-09-25', 'MALE', CURRENT_TIMESTAMP),
    (6, 6, 'Sara', 'Ahmed', '1994-02-18', 'FEMALE', CURRENT_TIMESTAMP),
    (7, 7, 'Mariam', 'Hassan', '1999-04-22', 'FEMALE', CURRENT_TIMESTAMP),
    (8, 8, 'Nour', 'Ali', '1997-06-14', 'FEMALE', CURRENT_TIMESTAMP),
    (9, 9, 'Hana', 'Ibrahim', '1993-08-30', 'FEMALE', CURRENT_TIMESTAMP),
    (10, 10, 'Salma', 'Mahmoud', '2000-10-05', 'FEMALE', CURRENT_TIMESTAMP),
    (11, 11, 'Mostafa', 'Adel', '1988-01-25', 'MALE', CURRENT_TIMESTAMP),
    (12, 12, 'Tarek', 'Fathy', '1991-03-17', 'MALE', CURRENT_TIMESTAMP),
    (13, 13, 'Amr', 'Nabil', '1995-05-29', 'MALE', CURRENT_TIMESTAMP),
    (14, 14, 'Hassan', 'Said', '1987-07-08', 'MALE', CURRENT_TIMESTAMP),
    (15, 15, 'Karim', 'Wael', '1999-09-19', 'MALE', CURRENT_TIMESTAMP),
    (16, 16, 'Aya', 'Maged', '1996-11-11', 'FEMALE', CURRENT_TIMESTAMP),
    (17, 17, 'Reem', 'Hany', '1998-12-03', 'FEMALE', CURRENT_TIMESTAMP),
    (18, 18, 'Dina', 'Ashraf', '1992-02-27', 'FEMALE', CURRENT_TIMESTAMP),
    (19, 19, 'Menna', 'Sherif', '1997-04-16', 'FEMALE', CURRENT_TIMESTAMP),
    (20, 20, 'Farah', 'Tamer', '2001-06-21', 'FEMALE', CURRENT_TIMESTAMP),
    (21, 21, 'Ibrahim', 'Salah', '1989-08-13', 'MALE', CURRENT_TIMESTAMP),
    (22, 22, 'Adham', 'Ramy', '1994-10-24', 'MALE', CURRENT_TIMESTAMP),
    (23, 23, 'Ziad', 'Ayman', '1996-12-15', 'MALE', CURRENT_TIMESTAMP),
    (24, 24, 'Mazen', 'Hisham', '1990-02-09', 'MALE', CURRENT_TIMESTAMP),
    (25, 25, 'Heba', 'Nasser', '1993-04-28', 'FEMALE', CURRENT_TIMESTAMP),
    (26, 26, 'Laila', 'Kamal', '1998-06-07', 'FEMALE', CURRENT_TIMESTAMP),
    (27, 27, 'Nada', 'Emad', '1995-08-19', 'FEMALE', CURRENT_TIMESTAMP),
    (28, 28, 'Jana', 'Sameh', '2000-10-31', 'FEMALE', CURRENT_TIMESTAMP),
    (29, 29, 'Yara', 'Osama', '1997-12-22', 'FEMALE', CURRENT_TIMESTAMP),
    (30, 30, 'Rana', 'Hamed', '1991-03-06', 'FEMALE', CURRENT_TIMESTAMP);

INSERT INTO doctors (
    id,
    user_id,
    department_id,
    first_name,
    last_name,
    specialization
)
VALUES
    (1, 31, 1, 'Ahmed', 'El-Sayed', 'Cardiologist'),
    (2, 32, 2, 'Mohamed', 'Hassan', 'Dermatologist'),
    (3, 33, 3, 'Omar', 'Ali', 'Neurologist'),
    (4, 34, 4, 'Khaled', 'Mahmoud', 'Pediatrician'),
    (5, 35, 5, 'Youssef', 'Ibrahim', 'General Physician'),
    (6, 36, 6, 'Mostafa', 'Adel', 'Orthopedic Surgeon'),
    (7, 37, 7, 'Tarek', 'Fathy', 'Ophthalmologist'),
    (8, 38, 8, 'Amr', 'Nabil', 'Dentist'),
    (9, 39, 9, 'Hassan', 'Said', 'Psychiatrist'),
    (10, 40, 10, 'Karim', 'Wael', 'Gastroenterologist'),
    (11, 41, 11, 'Ibrahim', 'Salah', 'Endocrinologist'),
    (12, 42, 12, 'Adham', 'Ramy', 'Pulmonologist'),
    (13, 43, 13, 'Ziad', 'Ayman', 'Urologist'),
    (14, 44, 14, 'Mazen', 'Hisham', 'Nephrologist'),
    (15, 45, 15, 'Sherif', 'Nasser', 'Oncologist'),
    (16, 46, 16, 'Hany', 'Ashraf', 'Radiologist'),
    (17, 47, 17, 'Sameh', 'Emad', 'Surgeon'),
    (18, 48, 18, 'Wael', 'Kamal', 'Anesthesiologist'),
    (19, 49, 19, 'Ramy', 'Salah', 'Rheumatologist'),
    (20, 50, 20, 'Ayman', 'Tamer', 'Hematologist'),
    (21, 51, 21, 'Nasser', 'Fathy', 'Infectious Disease Specialist'),
    (22, 52, 22, 'Ashraf', 'Maged', 'Immunologist'),
    (23, 53, 23, 'Emad', 'Hany', 'Plastic Surgeon'),
    (24, 54, 24, 'Salah', 'Ramy', 'Vascular Surgeon'),
    (25, 55, 25, 'Tamer', 'Said', 'Emergency Physician'),
    (26, 56, 26, 'Maged', 'Adel', 'Family Physician'),
    (27, 57, 27, 'Sherif', 'Hassan', 'Obstetrician'),
    (28, 58, 28, 'Hany', 'Mahmoud', 'Gynecologist'),
    (29, 59, 29, 'Fathy', 'Ibrahim', 'ENT Specialist'),
    (30, 60, 30, 'Adel', 'Nabil', 'Pathologist');
INSERT INTO availability_slots (
    doctor_id,
    date,
    start_time,
    end_time,
    status
)
VALUES
    (1,  '2026-10-01', '09:00:00', '09:30:00', 'AVAILABLE'),
    (1,  '2026-10-01', '09:30:00', '10:00:00', 'AVAILABLE'),
    (1,  '2026-10-01', '10:00:00', '10:30:00', 'AVAILABLE'),
    (2,  '2026-10-01', '09:30:00', '10:00:00', 'AVAILABLE'),
    (2,  '2026-10-01', '10:00:00', '10:30:00', 'AVAILABLE'),
    (2,  '2026-10-01', '11:00:00', '11:30:00', 'AVAILABLE'),
    (3,  '2026-10-01', '10:00:00', '10:30:00', 'AVAILABLE'),
    (4,  '2026-10-01', '10:30:00', '11:00:00', 'AVAILABLE'),
    (5,  '2026-10-01', '11:00:00', '11:30:00', 'AVAILABLE'),

    (6,  '2026-10-02', '09:00:00', '09:30:00', 'AVAILABLE'),
    (7,  '2026-10-02', '09:30:00', '10:00:00', 'AVAILABLE'),
    (8,  '2026-10-02', '10:00:00', '10:30:00', 'AVAILABLE'),
    (9,  '2026-10-02', '10:30:00', '11:00:00', 'AVAILABLE'),
    (10, '2026-10-02', '11:00:00', '11:30:00', 'AVAILABLE'),

    (11, '2026-10-03', '09:00:00', '09:30:00', 'AVAILABLE'),
    (12, '2026-10-03', '09:30:00', '10:00:00', 'AVAILABLE'),
    (13, '2026-10-03', '10:00:00', '10:30:00', 'AVAILABLE'),
    (14, '2026-10-03', '10:30:00', '11:00:00', 'AVAILABLE'),
    (15, '2026-10-03', '11:00:00', '11:30:00', 'AVAILABLE'),

    (16, '2026-10-04', '09:00:00', '09:30:00', 'AVAILABLE'),
    (17, '2026-10-04', '09:30:00', '10:00:00', 'AVAILABLE'),
    (18, '2026-10-04', '10:00:00', '10:30:00', 'AVAILABLE'),
    (19, '2026-10-04', '10:30:00', '11:00:00', 'AVAILABLE'),
    (20, '2026-10-04', '11:00:00', '11:30:00', 'AVAILABLE'),

    (21, '2026-10-05', '09:00:00', '09:30:00', 'AVAILABLE'),
    (22, '2026-10-05', '09:30:00', '10:00:00', 'AVAILABLE'),
    (23, '2026-10-05', '10:00:00', '10:30:00', 'AVAILABLE'),
    (24, '2026-10-05', '10:30:00', '11:00:00', 'AVAILABLE'),
    (25, '2026-10-05', '11:00:00', '11:30:00', 'AVAILABLE'),

    (26, '2026-10-06', '09:00:00', '09:30:00', 'AVAILABLE'),
    (27, '2026-10-06', '09:30:00', '10:00:00', 'AVAILABLE'),
    (28, '2026-10-06', '10:00:00', '10:30:00', 'AVAILABLE'),
    (29, '2026-10-06', '10:30:00', '11:00:00', 'AVAILABLE'),
    (30, '2026-10-06', '11:00:00', '11:30:00', 'AVAILABLE');

INSERT INTO appointments (
    patient_id,
    doctor_id,
    slot_id,
    status,
    created_at,
    notes
)
VALUES
    (1, 1, 1, 'CONFIRMED', CURRENT_TIMESTAMP, 'Appointment 1'),
    (2, 1, 2, 'CONFIRMED', CURRENT_TIMESTAMP, 'Appointment 1'),
    (3, 1, 3, 'CONFIRMED', CURRENT_TIMESTAMP, 'Appointment 1'),
    (1, 2, 2, 'CONFIRMED', CURRENT_TIMESTAMP, 'Appointment 2'),
    (1, 3, 3, 'CONFIRMED', CURRENT_TIMESTAMP, 'Appointment 3'),

    (2, 4, 4, 'CONFIRMED', CURRENT_TIMESTAMP, 'Appointment 1'),
    (2, 5, 5, 'CONFIRMED', CURRENT_TIMESTAMP, 'Appointment 2'),

    (3, 6, 6, 'CONFIRMED', CURRENT_TIMESTAMP, 'Appointment 1'),


    (5, 7, 7, 'CONFIRMED', CURRENT_TIMESTAMP, 'Appointment 1'),
    (5, 8, 8, 'CONFIRMED', CURRENT_TIMESTAMP, 'Appointment 2'),
    (5, 9, 9, 'CONFIRMED', CURRENT_TIMESTAMP, 'Appointment 3'),

    (6, 10, 10, 'CONFIRMED', CURRENT_TIMESTAMP, 'Appointment 1'),
    (6, 11, 11, 'CONFIRMED', CURRENT_TIMESTAMP, 'Appointment 2'),
    (6, 12, 12, 'CONFIRMED', CURRENT_TIMESTAMP, 'Appointment 3'),

    (7, 13, 13, 'CONFIRMED', CURRENT_TIMESTAMP, 'Appointment 1'),
    (7, 14, 14, 'CONFIRMED', CURRENT_TIMESTAMP, 'Appointment 2'),

    (8, 15, 15, 'CONFIRMED', CURRENT_TIMESTAMP, 'Appointment 1'),
    (8, 16, 16, 'CONFIRMED', CURRENT_TIMESTAMP, 'Appointment 2'),

    (9, 17, 17, 'CONFIRMED', CURRENT_TIMESTAMP, 'Appointment 1'),
    (9, 18, 18, 'CONFIRMED', CURRENT_TIMESTAMP, 'Appointment 2'),

    (10, 19, 19, 'CONFIRMED', CURRENT_TIMESTAMP, 'Appointment 1'),
    (10, 20, 20, 'CONFIRMED', CURRENT_TIMESTAMP, 'Appointment 2'),

    (11, 21, 21, 'CONFIRMED', CURRENT_TIMESTAMP, 'Appointment 1'),
    (11, 22, 22, 'CONFIRMED', CURRENT_TIMESTAMP, 'Appointment 2'),

    (12, 23, 23, 'CONFIRMED', CURRENT_TIMESTAMP, 'Appointment 1'),
    (12, 24, 24, 'CONFIRMED', CURRENT_TIMESTAMP, 'Appointment 2'),

    (13, 25, 25, 'CONFIRMED', CURRENT_TIMESTAMP, 'Appointment 1'),
    (13, 26, 26, 'CONFIRMED', CURRENT_TIMESTAMP, 'Appointment 2'),

    (14, 27, 27, 'CONFIRMED', CURRENT_TIMESTAMP, 'Appointment 1'),
    (14, 28, 28, 'CONFIRMED', CURRENT_TIMESTAMP, 'Appointment 2'),

    (15, 29, 29, 'CONFIRMED', CURRENT_TIMESTAMP, 'Appointment 1'),
    (15, 30, 30, 'CONFIRMED', CURRENT_TIMESTAMP, 'Appointment 2');

INSERT INTO referrals (
    id,
    appointment_id,
    referred_to_doctor_id,
    referral_reason,
    status,
    created_at
)
VALUES
    (1, 1, 2, 'Patient needs cardiology consultation.', 'PENDING', CURRENT_TIMESTAMP),
    (2, 2, 3, 'Patient requires neurological evaluation.', 'PENDING', CURRENT_TIMESTAMP),
    (3, 3, 4, 'Patient needs specialist follow-up.', 'PENDING', CURRENT_TIMESTAMP),
    (4, 4, 5, 'Patient requires additional medical assessment.', 'PENDING', CURRENT_TIMESTAMP),
    (5, 5, 6, 'Patient needs further diagnostic evaluation.', 'PENDING', CURRENT_TIMESTAMP),
    (6, 6, 7, 'Patient requires ophthalmology consultation.', 'PENDING', CURRENT_TIMESTAMP),
    (7, 7, 8, 'Patient needs dental specialist evaluation.', 'PENDING', CURRENT_TIMESTAMP),
    (8, 8, 9, 'Patient requires mental health assessment.', 'PENDING', CURRENT_TIMESTAMP),
    (9, 9, 10, 'Patient needs gastroenterology consultation.', 'PENDING', CURRENT_TIMESTAMP),
    (10, 10, 11, 'Patient requires endocrine evaluation.', 'PENDING', CURRENT_TIMESTAMP),
    (11, 11, 12, 'Patient needs pulmonary assessment.', 'PENDING', CURRENT_TIMESTAMP),
    (12, 12, 13, 'Patient requires urology consultation.', 'PENDING', CURRENT_TIMESTAMP),
    (13, 13, 14, 'Patient needs nephrology evaluation.', 'PENDING', CURRENT_TIMESTAMP),
    (14, 14, 15, 'Patient requires oncology follow-up.', 'PENDING', CURRENT_TIMESTAMP),
    (15, 15, 16, 'Patient needs radiology examination.', 'PENDING', CURRENT_TIMESTAMP),
    (16, 16, 17, 'Patient requires surgical assessment.', 'PENDING', CURRENT_TIMESTAMP),
    (17, 17, 18, 'Patient needs anesthesia consultation.', 'PENDING', CURRENT_TIMESTAMP),
    (18, 18, 19, 'Patient requires rheumatology evaluation.', 'PENDING', CURRENT_TIMESTAMP),
    (19, 19, 20, 'Patient needs hematology consultation.', 'PENDING', CURRENT_TIMESTAMP),
    (20, 20, 21, 'Patient requires infectious disease evaluation.', 'PENDING', CURRENT_TIMESTAMP),
    (21, 21, 22, 'Patient needs immunology consultation.', 'PENDING', CURRENT_TIMESTAMP),
    (22, 22, 23, 'Patient requires plastic surgery assessment.', 'PENDING', CURRENT_TIMESTAMP),
    (23, 23, 24, 'Patient needs vascular surgery consultation.', 'PENDING', CURRENT_TIMESTAMP),
    (24, 24, 25, 'Patient requires emergency medicine follow-up.', 'PENDING', CURRENT_TIMESTAMP),
    (25, 25, 26, 'Patient needs family medicine consultation.', 'PENDING', CURRENT_TIMESTAMP),
    (26, 26, 27, 'Patient requires obstetrics evaluation.', 'PENDING', CURRENT_TIMESTAMP),
    (27, 27, 28, 'Patient needs gynecology consultation.', 'PENDING', CURRENT_TIMESTAMP),
    (28, 28, 29, 'Patient requires ENT evaluation.', 'PENDING', CURRENT_TIMESTAMP),
    (29, 29, 30, 'Patient needs pathology assessment.', 'PENDING', CURRENT_TIMESTAMP),
    (30, 30, 1, 'Patient requires cardiac specialist follow-up.', 'PENDING', CURRENT_TIMESTAMP);