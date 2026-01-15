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
(3200.50, 'ES1200492352123456789012', 2),
(980.75, 'ES4501827364512345678901', 3),
(5400.00, 'ES9901827364512345678912', 4);

-- Credit Cards
INSERT INTO CreditCard (card_number, expiration_date, cvc, full_name, account_id) VALUES
('4111111111111111', '2027-12-31', '123', 'John Doe Smith', 1),
('5500000000000004', '2028-05-31', '456', 'Michael Roberts Johnson', 2),
('340000000000009', '2027-09-30', '789', 'Sandra Perez Lopez', 3),
('30000000000004', '2029-03-31', '321', 'Kevin Nguyen Tran', 4);

-- Bank Transactions
INSERT INTO BankTransaction (transaction_type_id, transaction_origin_id, source_card_id, transaction_date, amount, description, account_id) VALUES
(1, 3, 1, '2026-01-07', 50.00, 'Grocery store purchase', 1),
(2, 1, 1, '2026-01-06', 200.00, 'Salary deposit', 1),

(1, 3, 2, '2026-02-01', 120.00, 'Electronics store purchase', 2),
(2, 1, 2, '2026-02-03', 1800.00, 'Freelance payment', 2),
(1, 2, 2, '2026-02-05', 60.00, 'Gym membership', 2),

(2, 1, 3, '2026-01-15', 1500.00, 'Salary deposit', 3),
(1, 3, 3, '2026-01-16', 45.20, 'Restaurant dinner', 3),
(1, 2, 3, '2026-01-18', 89.99, 'Online subscription', 3),

(2, 1, 4, '2026-03-01', 2200.00, 'Salary deposit', 4),
(1, 3, 4, '2026-03-02', 150.00, 'Clothing store purchase', 4),
(1, 3, 4, '2026-03-04', 12.50, 'Coffee shop', 4),
(2, 1, 4, '2026-03-05', 300.00, 'Refund from retailer', 4);
