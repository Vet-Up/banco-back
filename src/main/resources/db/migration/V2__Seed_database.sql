-- ============================================
-- INSERT DATA
-- ============================================

-- Transaction Types
INSERT INTO BankTransactionType (transaction_type_id, name) VALUES
(1, 'Debit'),
(2, 'Credit');

-- Transaction Origins
INSERT INTO BankTransactionOrigin (transaction_origin_id, name) VALUES
(1, 'Transfer'),
(2, 'DirectDebit'),
(3, 'BankCard');

-- Users
-- hashedpass123 pass456 pass789 pass999
INSERT INTO Users (username, password, first_name, last_name1, last_name2, dni, api_key) VALUES
('jdoe', '$2a$12$aEq5AHMRIUYPy.0qWlFlLeiEglzA90Ith4J6/imEYAot8XFcBeWhm', 'John', 'Doe', 'Smith', '12345678A', 'token_jdoe_001'),
('mroberts', '$2a$12$rukUvX9WF6pKizhupQ5X7.YwQzVtJ8lyuvQaXHOVT87TsP6G2/ISG', 'Michael', 'Roberts', 'Johnson', '87654321B', 'token_mroberts_002'),
('sandra.p', '$2a$12$9pCXHTM5r7FAl6tbxwjf3.OiObcrBtf/SRvWVUXRoYwOPSIrZcpI6', 'Sandra', 'Perez', 'Lopez', '11223344C', 'token_sandra_003'),
('knguyen', '$2a$12$YYcp34vni8ewz9bs1NgaseqiybX9DIT2/zMo.OqbRZHXUs4ws/Uui', 'Kevin', 'Nguyen', 'Tran', '99887766D', 'token_knguyen_004');

-- Bank Accounts
INSERT INTO BankAccount (balance, iban, user_id) VALUES
(1500.00, 'ES7620770024003102575766', 1),
(1500.00, 'ES7620770024003102575764', 1),
(3200.50, 'ES1200492352123456789012', 2),
(980.75, 'ES4501827364512345678901', 3),
(5400.00, 'ES9901827364512345678912', 4),
(2750.25, 'ES6621000418401234567891', 1),
(8500.00, 'ES9121000418450200051332', 2),
(1200.00, 'ES7921000813610123456789', 3),
(4300.75, 'ES3114650100722030876293', 4),
(650.50, 'ES0049000123456789012345', 1),
(12000.00, 'ES8200810012345678901234', 2),
(3890.25, 'ES5930040012345678901234', 3);

-- Credit Cards
INSERT INTO CreditCard (card_number, expiration_date, cvc, full_name, account_id) VALUES
('4111111111111111', '2027-12-01', '123', 'John Doe Smith', 1),
('4111111111111121', '2028-11-01', '123', 'John Doe Smith', 1),
('5500000000000004', '2028-05-01', '456', 'Michael Roberts Johnson', 3),
('340000000000009', '2027-09-01', '789', 'Sandra Perez Lopez', 4),
('30000000000004', '2029-03-01', '321', 'Kevin Nguyen Tran', 5),
('30000000000005', '2029-03-01', '321', 'Kevin Nguyen Tran', 5),
('4532015112830366', '2028-08-01', '542', 'John Doe Smith', 6),
('4716182333661786', '2027-11-01', '887', 'Michael Roberts Johnson', 7),
('5425233430109903', '2028-04-01', '234', 'Sandra Perez Lopez', 8),
('4929598581234567', '2029-01-01', '156', 'Kevin Nguyen Tran', 9),
('4539578763621486', '2027-07-01', '923', 'John Doe Smith', 10),
('4916338506082832', '2028-10-01', '445', 'Michael Roberts Johnson', 11),
('4024007198642889', '2029-06-01', '712', 'Sandra Perez Lopez', 12),
('4556737586899855', '2026-03-01', '331', 'Sandra Perez Lopez', 12);

-- Bank Transactions
INSERT INTO BankTransaction (transaction_type_id, transaction_origin_id, source_card_id, transaction_date, amount, description, account_id) VALUES
-- Transactions account 1 (John Doe - account 1)
(1, 3, 1, '2026-01-07', 50.00, 'Grocery store purchase', 1),
(2, 1, 1, '2026-01-06', 200.00, 'Salary deposit', 1),
(1, 3, 1, '2026-01-08', 35.50, 'Gas station', 1),
(1, 2, 1, '2026-01-10', 89.99, 'Netflix subscription', 1),
(2, 1, 1, '2026-01-15', 1500.00, 'Monthly salary', 1),

-- Transactions account 2 (John Doe - account 2) (dates moved earlier)
(1, 3, 2, '2026-01-02', 120.00, 'Electronics store purchase', 2),
(2, 1, 2, '2026-01-03', 1800.00, 'Freelance payment', 2),
(1, 2, 2, '2026-01-05', 60.00, 'Gym membership', 2),
(1, 3, 2, '2026-01-06', 45.00, 'Book store', 2),

-- Transactions account 3 (Michael Roberts) (some dates moved earlier)
(2, 1, 3, '2026-01-15', 1500.00, 'Salary deposit', 3),
(1, 3, 3, '2026-01-16', 45.20, 'Restaurant dinner', 3),
(1, 2, 3, '2026-01-14', 89.99, 'Online subscription', 3),
(1, 3, 3, '2026-01-13', 230.00, 'Furniture store', 3),
(2, 1, 3, '2026-01-12', 500.00, 'Bonus payment', 3),

-- Transactions account 4 (Sandra Perez) (moved from March to January)
(2, 1, 4, '2026-01-04', 2200.00, 'Salary deposit', 4),
(1, 3, 4, '2026-01-05', 150.00, 'Clothing store purchase', 4),
(1, 3, 4, '2026-01-11', 12.50, 'Coffee shop', 4),
(2, 1, 4, '2026-01-09', 300.00, 'Refund from retailer', 4),

-- Transactions account 5 (Kevin Nguyen)
(2, 1, 5, '2026-01-01', 3000.00, 'Initial deposit', 5),
(1, 3, 5, '2026-01-05', 150.00, 'Supermarket', 5),
(1, 2, 5, '2026-01-10', 45.00, 'Spotify premium', 5),

-- Transactions account 5 with card 6 (Kevin Nguyen)
(2, 1, 6, '2026-01-02', 5000.00, 'Transfer received', 5),
(1, 3, 6, '2026-01-08', 320.00, 'Electronics purchase', 5),

-- Transactions account 6 (John Doe - account 6)
(2, 1, 7, '2026-01-03', 2500.00, 'Salary deposit', 6),
(1, 3, 7, '2026-01-07', 78.50, 'Pharmacy', 6),
(1, 2, 7, '2026-01-12', 120.00, 'Insurance payment', 6),
(1, 3, 7, '2026-01-14', 55.00, 'Restaurant', 6),

-- Transactions account 7 (Michael Roberts - account 7)
(2, 1, 8, '2026-01-05', 1800.00, 'Freelance income', 7),
(1, 3, 8, '2026-01-09', 95.00, 'Online shopping', 7),

-- Transactions account 8 (Sandra Perez - account 8)
(2, 1, 9, '2026-01-01', 800.00, 'Initial deposit', 8),
(1, 3, 9, '2026-01-06', 25.00, 'Coffee shop', 8),
(1, 2, 9, '2026-01-11', 35.00, 'Cloud storage', 8),

-- Transactions account 9 (Kevin Nguyen - account 9)
(2, 1, 10, '2026-01-02', 10000.00, 'Savings transfer', 9),
(1, 3, 10, '2026-01-10', 450.00, 'Flight tickets', 9),
(1, 3, 10, '2026-01-12', 200.00, 'Hotel booking', 9),

-- Transactions account 10 (John Doe - account 10)
(2, 1, 11, '2026-01-04', 4500.00, 'Salary deposit', 10),
(1, 3, 11, '2026-01-08', 180.00, 'Groceries', 10),
(1, 2, 11, '2026-01-13', 65.00, 'Mobile phone bill', 10),
(1, 3, 11, '2026-01-15', 42.00, 'Pet supplies', 10),
(2, 1, 11, '2026-01-11', 250.00, 'Cashback reward', 10),

-- Transactions account 11 (Michael Roberts - account 11)
(2, 1, 12, '2026-01-03', 6000.00, 'Investment return', 11),
(1, 3, 12, '2026-01-06', 350.00, 'Appliance purchase', 11),
(1, 2, 12, '2026-01-09', 99.99, 'Annual subscription', 11),
(1, 3, 12, '2026-01-11', 28.50, 'Taxi ride', 11),

-- Transactions account 12 (Sandra Perez - account 12)
(2, 1, 13, '2026-01-02', 2800.00, 'Contract payment', 12),
(1, 3, 13, '2026-01-05', 125.00, 'Medical checkup', 12),
(1, 3, 13, '2026-01-08', 67.30, 'Supermarket', 12),
(1, 2, 13, '2026-01-12', 49.99, 'Magazine subscription', 12),

-- More transactions with card 14 (Sandra Perez - account 12)
(1, 3, 14, '2026-01-04', 89.00, 'Online course', 12),
(1, 3, 14, '2026-01-07', 42.50, 'Hair salon', 12),
(2, 1, 14, '2026-01-14', 150.00, 'Refund', 12);
