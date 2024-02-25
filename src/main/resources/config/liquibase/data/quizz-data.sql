INSERT INTO quizz(id, title, description, difficulty, category, published, question_order, max_answer_time, allow_back, allow_review, keep_answers_secret, publish_date, attemps_limit, attemps_limit_period, question_count, user_id) VALUES 
('7bf9fa79-5b46-4bb0-bb38-298bf9bd036b', 'My first Quizz', 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Maecenas venenatis ipsum nisi, eu ullamcorper tortor suscipit eget. Donec hendrerit, justo blandit lobortis egestas, magna ex accumsan diam, sit amet molestie nisi diam id urna. Cras tortor mauris, dictum quis imperdiet ut, sollicitudin in quam. Mauris sit amet nisl sit amet felis dictum dapibus congue ac nisi. Ut ut justo eget neque placerat tempus nec ut velit. Integer mattis urna sed mi fringilla, vel pretium felis lobortis.', 
'EASY', 'GENERAL_KNOWLEDGE', true, 'FIXED', 0, true, true, false, TIMESTAMP '2023-11-01T18:00:00', 0, null, 3, 2);

INSERT INTO question(id, statement, index, correct_option_index, quizz_id) VALUES
('ea641920-1492-4cfd-b8ef-e0f58d5d9dc2', 'Que mesure l''année lumière ?', 2, 3, '7bf9fa79-5b46-4bb0-bb38-298bf9bd036b'),
('598d3310-fa70-4409-89b9-c48b7601774f', 'Lequel de ces éléments est nécessaire pour faire de l''énergie nucléaire et des armes nucléaires ?', 1, 2, '7bf9fa79-5b46-4bb0-bb38-298bf9bd036b'),
('bcd7a793-9ca3-4a9d-b17d-3398b2eb0632', 'Lequel de ces termes défini l''étude de l''influence de la position des étoiles et des planètes sur le comportement humain ?', 3, 1, '7bf9fa79-5b46-4bb0-bb38-298bf9bd036b');

INSERT INTO option(id, statement, index, question_id) VALUES
('f9a845bd-3e13-48aa-a01c-2655789720df', 'La luminosité', 1, 'ea641920-1492-4cfd-b8ef-e0f58d5d9dc2'),
('18aced7a-84ba-4179-a7af-c1e86ba069a2', 'Le temps', 2, 'ea641920-1492-4cfd-b8ef-e0f58d5d9dc2'),
('bc1fd616-fa22-4de7-84b5-657414ad54ee', 'La distance', 3, 'ea641920-1492-4cfd-b8ef-e0f58d5d9dc2'),
('38aa6ead-d352-4616-a82b-85cbdbda6c9d', 'Le poids', 4, 'ea641920-1492-4cfd-b8ef-e0f58d5d9dc2'),
('0c872e54-f570-42eb-9e7e-c75f219d7d39', 'Chlorure de sodium', 1, '598d3310-fa70-4409-89b9-c48b7601774f'),
('8a50db4f-603c-4247-b1e1-1e5532ffabb6', 'Uranium', 2, '598d3310-fa70-4409-89b9-c48b7601774f'),
('b6a6d662-6b5e-4a66-a369-9faa50ccb2dd', 'Azote', 3, '598d3310-fa70-4409-89b9-c48b7601774f'),
('9a9fffe2-03af-4ea4-9920-d0b5bbf76397', 'Dioxide de carbone', 4, '598d3310-fa70-4409-89b9-c48b7601774f'),
('b15b3f58-28fc-4f4b-b215-dfc399cda857', 'Astrologie', 1, 'bcd7a793-9ca3-4a9d-b17d-3398b2eb0632'),
('ffc4eb20-2f64-4e49-9d1f-d711e3d6a565', 'Alchimie', 2, 'bcd7a793-9ca3-4a9d-b17d-3398b2eb0632'),
('f1ab39a0-b5d7-421a-b94a-53e4e6e47f04', 'Astronomie', 3, 'bcd7a793-9ca3-4a9d-b17d-3398b2eb0632'),
('cbc3416b-de83-42cd-8405-92ddf9e88c77', 'Météorologie', 4, 'bcd7a793-9ca3-4a9d-b17d-3398b2eb0632');

INSERT INTO attempt(id, correct_answer_count, wrong_answer_count, unanswered_count, started, ended, quizz_id, user_id) VALUES
('f34f26a1-e0ce-4a71-9043-6858e4b387d6', 0, 3, 0, TIMESTAMP '2023-11-29T23:00:00', TIMESTAMP '2023-11-29T23:11:05', '7bf9fa79-5b46-4bb0-bb38-298bf9bd036b', 2),
('80adba88-c349-4e92-b719-a60b4dd3987b', 3, 0, 0, TIMESTAMP '2023-11-30T15:40:00', TIMESTAMP '2023-11-30T15:45:05', '7bf9fa79-5b46-4bb0-bb38-298bf9bd036b', 1);

INSERT INTO attempt_answer(id, started, ended, correct, question_id, option_id, attempt_id) VALUES
('529f0916-bd52-422e-8ae0-f8fcb698321e', TIMESTAMP '2023-11-29T23:00:00', TIMESTAMP '2023-11-29T23:02:05', false, 'ea641920-1492-4cfd-b8ef-e0f58d5d9dc2', 'f9a845bd-3e13-48aa-a01c-2655789720df', 'f34f26a1-e0ce-4a71-9043-6858e4b387d6'),
('3894e19e-ae62-4cc4-ae1e-56abe359f9a2', TIMESTAMP '2023-11-29T23:02:00', TIMESTAMP '2023-11-29T23:06:05', false, '598d3310-fa70-4409-89b9-c48b7601774f', '0c872e54-f570-42eb-9e7e-c75f219d7d39', 'f34f26a1-e0ce-4a71-9043-6858e4b387d6'),
('ca461a57-c7a3-4279-9bdb-e341011b493b', TIMESTAMP '2023-11-29T23:06:00', TIMESTAMP '2023-11-29T23:11:05', false, 'bcd7a793-9ca3-4a9d-b17d-3398b2eb0632', 'ffc4eb20-2f64-4e49-9d1f-d711e3d6a565', 'f34f26a1-e0ce-4a71-9043-6858e4b387d6'),
('3d4d7e9a-f628-4653-8616-4f79dd629716', TIMESTAMP '2023-11-30T15:40:00', TIMESTAMP '2023-11-30T15:41:05', true, 'ea641920-1492-4cfd-b8ef-e0f58d5d9dc2', 'bc1fd616-fa22-4de7-84b5-657414ad54ee', '80adba88-c349-4e92-b719-a60b4dd3987b'),
('9637d2b2-b5e4-4237-bbe7-d8192c81d7fe', TIMESTAMP '2023-11-30T15:41:00', TIMESTAMP '2023-11-30T15:43:05', true, '598d3310-fa70-4409-89b9-c48b7601774f', '8a50db4f-603c-4247-b1e1-1e5532ffabb6', '80adba88-c349-4e92-b719-a60b4dd3987b'),
('9804a433-3a84-4974-ba4c-6e19181a809b', TIMESTAMP '2023-11-30T15:43:00', TIMESTAMP '2023-11-30T15:45:05', true, 'bcd7a793-9ca3-4a9d-b17d-3398b2eb0632', 'b15b3f58-28fc-4f4b-b215-dfc399cda857', '80adba88-c349-4e92-b719-a60b4dd3987b');



 
 
 
 
 
 
 
 
 
 
 
 
 


