-- This file inserts initial dummy data into the todos table when application starts

INSERT INTO todos (title, description, status, created_at)
VALUES
('watch IPL match', 'IPL starts at 7 pm', 'PENDING', CURRENT_TIMESTAMP),
('Do Study', 'atleast for 2 hrs', 'PENDING', CURRENT_TIMESTAMP),
('Go to Gym', 'evening workout', 'COMPLETED', CURRENT_TIMESTAMP),
('Sleep Early', 'Plan failed successfully', 'COMPLETED', CURRENT_TIMESTAMP);
