INSERT INTO `tenants` (name) VALUES ('Charles');
INSERT INTO `tenants` (name) VALUES ('Billy');

INSERT INTO `properties` (name) VALUES ('Huge Mansion');
INSERT INTO `properties` (name) VALUES ('The Castle');

INSERT INTO `blocks` (property_id, start_date, end_date) VALUES (1, '2022-02-20', '2022-02-22');
INSERT INTO `blocks` (property_id, start_date, end_date) VALUES (2, '2022-02-20', '2022-02-22');
INSERT INTO `blocks` (property_id, start_date, end_date) VALUES (2, '2022-02-21', '2022-02-23');

INSERT INTO `bookings` (property_id, tenant_id, start_date, end_date) VALUES (1, 1, '2022-02-23', '2022-02-25');
INSERT INTO `bookings` (property_id, tenant_id, start_date, end_date) VALUES (2, 2, '2022-02-23', '2022-02-25');
