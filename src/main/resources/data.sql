-- ============================================================
-- MediTrack Development Seed Data
-- Release 1 - Patient Booking MVP
-- ============================================================
--
-- IMPORTANT:
-- This file is intentionally safe to run on application startup.
-- It does NOT truncate existing application data.
--
-- Development login password for seeded users:
-- pass123
--
-- Passwords below are BCrypt hashes for "pass123".
-- ============================================================


-- ============================================================
-- DEPARTMENTS
-- ============================================================

INSERT INTO departments (name, description)
SELECT 'Cardiology', 'Heart and cardiovascular diseases'
WHERE NOT EXISTS (
    SELECT 1 FROM departments WHERE name = 'Cardiology'
);

INSERT INTO departments (name, description)
SELECT 'Pediatrics', 'Children healthcare and diseases'
WHERE NOT EXISTS (
    SELECT 1 FROM departments WHERE name = 'Pediatrics'
);

INSERT INTO departments (name, description)
SELECT 'Neurology', 'Brain and nervous system treatment'
WHERE NOT EXISTS (
    SELECT 1 FROM departments WHERE name = 'Neurology'
);

INSERT INTO departments (name, description)
SELECT 'Orthopedics', 'Bones and joint surgeries'
WHERE NOT EXISTS (
    SELECT 1 FROM departments WHERE name = 'Orthopedics'
);


-- ============================================================
-- DOCTOR USERS
-- ============================================================

INSERT INTO users (
    username,
    password_hash,
    phone,
    role,
    status,
    created_at
)
SELECT
    'dr_ahmed',
    '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy',
    '01011112222',
    'DOCTOR',
    'ACTIVE',
    CURRENT_TIMESTAMP
WHERE NOT EXISTS (
    SELECT 1 FROM users WHERE phone = '01011112222'
);

INSERT INTO users (
    username,
    password_hash,
    phone,
    role,
    status,
    created_at
)
SELECT
    'dr_sara',
    '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy',
    '01022223333',
    'DOCTOR',
    'ACTIVE',
    CURRENT_TIMESTAMP
WHERE NOT EXISTS (
    SELECT 1 FROM users WHERE phone = '01022223333'
);

INSERT INTO users (
    username,
    password_hash,
    phone,
    role,
    status,
    created_at
)
SELECT
    'dr_mora',
    '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy',
    '01033334444',
    'DOCTOR',
    'ACTIVE',
    CURRENT_TIMESTAMP
WHERE NOT EXISTS (
    SELECT 1 FROM users WHERE phone = '01033334444'
);

INSERT INTO users (
    username,
    password_hash,
    phone,
    role,
    status,
    created_at
)
SELECT
    'dr_omar',
    '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy',
    '01044445555',
    'DOCTOR',
    'ACTIVE',
    CURRENT_TIMESTAMP
WHERE NOT EXISTS (
    SELECT 1 FROM users WHERE phone = '01044445555'
);

INSERT INTO users (
    username,
    password_hash,
    phone,
    role,
    status,
    created_at
)
SELECT
    'dr_mostafa',
    '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy',
    '01066667777',
    'DOCTOR',
    'ACTIVE',
    CURRENT_TIMESTAMP
WHERE NOT EXISTS (
    SELECT 1 FROM users WHERE phone = '01066667777'
);


-- ============================================================
-- DOCTORS
-- ============================================================

INSERT INTO doctors (
    user_id,
    department_id,
    first_name,
    last_name,
    specialization
)
SELECT
    u.id,
    d.id,
    'Ahmed',
    'Abdelhalem',
    'Interventional Cardiologist'
FROM users u
JOIN departments d ON d.name = 'Cardiology'
WHERE u.phone = '01011112222'
  AND NOT EXISTS (
      SELECT 1 FROM doctors doc WHERE doc.user_id = u.id
  );

INSERT INTO doctors (
    user_id,
    department_id,
    first_name,
    last_name,
    specialization
)
SELECT
    u.id,
    d.id,
    'Sara',
    'Mahmoud',
    'General Cardiologist'
FROM users u
JOIN departments d ON d.name = 'Cardiology'
WHERE u.phone = '01022223333'
  AND NOT EXISTS (
      SELECT 1 FROM doctors doc WHERE doc.user_id = u.id
  );

INSERT INTO doctors (
    user_id,
    department_id,
    first_name,
    last_name,
    specialization
)
SELECT
    u.id,
    d.id,
    'Mora',
    'Ibrahim',
    'Pediatric Specialist'
FROM users u
JOIN departments d ON d.name = 'Pediatrics'
WHERE u.phone = '01033334444'
  AND NOT EXISTS (
      SELECT 1 FROM doctors doc WHERE doc.user_id = u.id
  );

INSERT INTO doctors (
    user_id,
    department_id,
    first_name,
    last_name,
    specialization
)
SELECT
    u.id,
    d.id,
    'Omar',
    'Hassan',
    'Neurologist'
FROM users u
JOIN departments d ON d.name = 'Neurology'
WHERE u.phone = '01044445555'
  AND NOT EXISTS (
      SELECT 1 FROM doctors doc WHERE doc.user_id = u.id
  );

INSERT INTO doctors (
    user_id,
    department_id,
    first_name,
    last_name,
    specialization
)
SELECT
    u.id,
    d.id,
    'Mostafa',
    'Khaled',
    'Pediatric Specialist'
FROM users u
JOIN departments d ON d.name = 'Pediatrics'
WHERE u.phone = '01066667777'
  AND NOT EXISTS (
      SELECT 1 FROM doctors doc WHERE doc.user_id = u.id
  );


-- ============================================================
-- PATIENT USER
-- ============================================================
--
-- This seeded patient is ACTIVE because it represents an already
-- verified patient account.
--
-- New patients created through /api/auth/register remain INACTIVE
-- until OTP verification.
-- ============================================================

INSERT INTO users (
    username,
    password_hash,
    phone,
    role,
    status,
    created_at
)
SELECT
    'patient_mostafa',
    '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy',
    '01055556666',
    'PATIENT',
    'ACTIVE',
    CURRENT_TIMESTAMP
WHERE NOT EXISTS (
    SELECT 1 FROM users WHERE phone = '01055556666'
);


-- ============================================================
-- PATIENT
-- ============================================================

INSERT INTO patients (
    user_id,
    first_name,
    last_name,
    date_of_birth,
    gender,
    created_at
)
SELECT
    u.id,
    'Mostafa',
    'Kharbita',
    '2005-01-01',
    'MALE',
    CURRENT_TIMESTAMP
FROM users u
WHERE u.phone = '01055556666'
  AND NOT EXISTS (
      SELECT 1 FROM patients p WHERE p.user_id = u.id
  );


-- ============================================================
-- AVAILABILITY SLOTS
-- ============================================================

INSERT INTO availability_slots (
    doctor_id,
    date,
    start_time,
    end_time,
    status
)
SELECT
    d.id,
    '2026-09-11',
    '09:00:00',
    '09:30:00',
    'AVAILABLE'
FROM doctors d
JOIN users u ON u.id = d.user_id
WHERE u.phone = '01011112222'
  AND NOT EXISTS (
      SELECT 1
      FROM availability_slots s
      WHERE s.doctor_id = d.id
        AND s.date = '2026-09-11'
        AND s.start_time = '09:00:00'
  );

INSERT INTO availability_slots (
    doctor_id,
    date,
    start_time,
    end_time,
    status
)
SELECT
    d.id,
    '2026-09-11',
    '09:30:00',
    '10:00:00',
    'AVAILABLE'
FROM doctors d
JOIN users u ON u.id = d.user_id
WHERE u.phone = '01011112222'
  AND NOT EXISTS (
      SELECT 1
      FROM availability_slots s
      WHERE s.doctor_id = d.id
        AND s.date = '2026-09-11'
        AND s.start_time = '09:30:00'
  );

INSERT INTO availability_slots (
    doctor_id,
    date,
    start_time,
    end_time,
    status
)
SELECT
    d.id,
    '2026-09-11',
    '10:00:00',
    '10:30:00',
    'BOOKED'
FROM doctors d
JOIN users u ON u.id = d.user_id
WHERE u.phone = '01011112222'
  AND NOT EXISTS (
      SELECT 1
      FROM availability_slots s
      WHERE s.doctor_id = d.id
        AND s.date = '2026-09-11'
        AND s.start_time = '10:00:00'
  );

INSERT INTO availability_slots (
    doctor_id,
    date,
    start_time,
    end_time,
    status
)
SELECT
    d.id,
    '2026-09-11',
    '11:00:00',
    '11:30:00',
    'AVAILABLE'
FROM doctors d
JOIN users u ON u.id = d.user_id
WHERE u.phone = '01022223333'
  AND NOT EXISTS (
      SELECT 1
      FROM availability_slots s
      WHERE s.doctor_id = d.id
        AND s.date = '2026-09-11'
        AND s.start_time = '11:00:00'
  );

INSERT INTO availability_slots (
    doctor_id,
    date,
    start_time,
    end_time,
    status
)
SELECT
    d.id,
    '2026-09-11',
    '12:00:00',
    '12:30:00',
    'AVAILABLE'
FROM doctors d
JOIN users u ON u.id = d.user_id
WHERE u.phone = '01022223333'
  AND NOT EXISTS (
      SELECT 1
      FROM availability_slots s
      WHERE s.doctor_id = d.id
        AND s.date = '2026-09-11'
        AND s.start_time = '12:00:00'
  );

INSERT INTO availability_slots (
    doctor_id,
    date,
    start_time,
    end_time,
    status
)
SELECT
    d.id,
    '2026-09-11',
    '14:00:00',
    '14:30:00',
    'AVAILABLE'
FROM doctors d
JOIN users u ON u.id = d.user_id
WHERE u.phone = '01033334444'
  AND NOT EXISTS (
      SELECT 1
      FROM availability_slots s
      WHERE s.doctor_id = d.id
        AND s.date = '2026-09-11'
        AND s.start_time = '14:00:00'
  );

INSERT INTO availability_slots (
    doctor_id,
    date,
    start_time,
    end_time,
    status
)
SELECT
    d.id,
    '2026-09-11',
    '14:30:00',
    '15:00:00',
    'AVAILABLE'
FROM doctors d
JOIN users u ON u.id = d.user_id
WHERE u.phone = '01033334444'
  AND NOT EXISTS (
      SELECT 1
      FROM availability_slots s
      WHERE s.doctor_id = d.id
        AND s.date = '2026-09-11'
        AND s.start_time = '14:30:00'
  );

INSERT INTO availability_slots (
    doctor_id,
    date,
    start_time,
    end_time,
    status
)
SELECT
    d.id,
    '2026-09-11',
    '16:00:00',
    '16:30:00',
    'BOOKED'
FROM doctors d
JOIN users u ON u.id = d.user_id
WHERE u.phone = '01044445555'
  AND NOT EXISTS (
      SELECT 1
      FROM availability_slots s
      WHERE s.doctor_id = d.id
        AND s.date = '2026-09-11'
        AND s.start_time = '16:00:00'
  );