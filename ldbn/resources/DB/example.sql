INSERT INTO assignment (user_id, name, xml) VALUES
(1, 'test-minimal-cover', '<ldbn type="assignment"><att>A</att><att>B</att><att>C</att><att>D</att><att>E</att><att>G</att><fd><lhs><fdatt>A</fdatt><fdatt>B</fdatt></lhs><rhs><fdatt>C</fdatt></rhs></fd><fd><lhs><fdatt>C</fdatt></lhs><rhs><fdatt>A</fdatt></rhs></fd><fd><lhs><fdatt>B</fdatt><fdatt>C</fdatt></lhs><rhs><fdatt>D</fdatt></rhs></fd><fd><lhs><fdatt>A</fdatt><fdatt>C</fdatt><fdatt>D</fdatt></lhs><rhs><fdatt>B</fdatt></rhs></fd><fd><lhs><fdatt>D</fdatt></lhs><rhs><fdatt>E</fdatt></rhs></fd><fd><lhs><fdatt>D</fdatt></lhs><rhs><fdatt>G</fdatt></rhs></fd><fd><lhs><fdatt>B</fdatt><fdatt>E</fdatt></lhs><rhs><fdatt>C</fdatt></rhs></fd><fd><lhs><fdatt>C</fdatt><fdatt>G</fdatt></lhs><rhs><fdatt>B</fdatt></rhs></fd><fd><lhs><fdatt>C</fdatt><fdatt>G</fdatt></lhs><rhs><fdatt>D</fdatt></rhs></fd><fd><lhs><fdatt>C</fdatt><fdatt>E</fdatt></lhs><rhs><fdatt>A</fdatt></rhs></fd><fd><lhs><fdatt>C</fdatt><fdatt>E</fdatt></lhs><rhs><fdatt>G</fdatt></rhs></fd></ldbn>'),
(1, 'test-1', '<ldbn type="assignment"><att>A</att><att>B</att><att>C</att><att>D</att><fd><lhs><fdatt>A</fdatt></lhs><rhs><fdatt>B</fdatt></rhs></fd><fd><lhs><fdatt>B</fdatt></lhs><rhs><fdatt>C</fdatt><fdatt>D</fdatt></rhs></fd></ldbn>');

//BUG TEST
INSERT INTO assignment (user_id, name, xml) VALUES
(1, 'test3', '<ldbn type="assignment"><att>A</att><att>B</att><att>C</att><att>D</att><att>E</att><fd><lhs><fdatt>C</fdatt><fdatt>E</fdatt></lhs><rhs><fdatt>A</fdatt></rhs></fd><fd><lhs><fdatt>C</fdatt><fdatt>E</fdatt></lhs><rhs/></fd><fd><lhs><fdatt>B</fdatt><fdatt>C</fdatt></lhs><rhs><fdatt>D</fdatt></rhs></fd><fd><lhs><fdatt>C</fdatt></lhs><rhs><fdatt>B</fdatt></rhs></fd><fd><lhs><fdatt>D</fdatt></lhs><rhs/></fd><fd><lhs><fdatt>B</fdatt><fdatt>E</fdatt></lhs><rhs><fdatt>C</fdatt></rhs></fd><fd><lhs><fdatt>D</fdatt></lhs><rhs/></fd><fd><lhs><fdatt>C</fdatt><fdatt>E</fdatt></lhs><rhs><fdatt>A</fdatt></rhs></fd><fd><lhs><fdatt>B</fdatt><fdatt>C</fdatt></lhs><rhs><fdatt>D</fdatt></rhs></fd><fd><lhs><fdatt>A</fdatt><fdatt>B</fdatt></lhs><rhs><fdatt>C</fdatt></rhs></fd><fd><lhs><fdatt>C</fdatt></lhs><rhs><fdatt>D</fdatt></rhs></fd><fd><lhs><fdatt>B</fdatt><fdatt>E</fdatt></lhs><rhs><fdatt>C</fdatt></rhs></fd><fd><lhs><fdatt>A</fdatt><fdatt>C</fdatt><fdatt>D</fdatt></lhs><rhs><fdatt>B</fdatt></rhs></fd><fd><lhs><fdatt>C</fdatt></lhs><rhs><fdatt>D</fdatt></rhs></fd><fd><lhs><fdatt>C</fdatt><fdatt>E</fdatt></lhs><rhs/></fd><fd><lhs><fdatt>C</fdatt></lhs><rhs><fdatt>A</fdatt></rhs></fd><fd><lhs><fdatt>D</fdatt></lhs><rhs><fdatt>E</fdatt></rhs></fd><fd><lhs><fdatt>A</fdatt><fdatt>B</fdatt></lhs><rhs><fdatt>C</fdatt></rhs></fd><fd><lhs><fdatt>A</fdatt><fdatt>C</fdatt><fdatt>D</fdatt></lhs><rhs><fdatt>B</fdatt></rhs></fd><fd><lhs><fdatt>D</fdatt></lhs><rhs><fdatt>E</fdatt></rhs></fd><fd><lhs><fdatt>C</fdatt></lhs><rhs><fdatt>B</fdatt></rhs></fd><fd><lhs><fdatt>C</fdatt></lhs><rhs><fdatt>A</fdatt></rhs></fd></ldbn>\r\n');


//user admin with password ldbn
INSERT INTO user (name, email, pass_md5, active) VALUES
('admin', 'admin@ldbnonline.com', '46cd73323a9b7c4df4ca30ccc4f60426', 1);

//user hegner with password ldbn
INSERT INTO user (name, email, pass_md5, active) VALUES
('hegner', 'hegner@cs.umu.se', '46cd73323a9b7c4df4ca30ccc4f60426', 1);


//alter assinment
ALTER TABLE assignment 
CHANGE added_on modified_on 
TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP