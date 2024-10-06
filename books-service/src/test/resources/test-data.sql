DELETE FROM order_items; 
DELETE FROM orders;      
DELETE FROM tokens;
DELETE FROM notifications;
DELETE FROM users;
DELETE FROM books;
INSERT INTO books(code, name, description, image_url, price) VALUES
      ('P100','The Hunger Games','Winning will make you famous. Losing means certain death...','https://images.gr-assets.com/books/1447303603l/2767052.jpg', 34.0),
      ('P101','To Kill a Mockingbird','The unforgettable novel OF a childhood IN a sleepy Southern town AND the crisis OF conscience that rocked it...','https://images.gr-assets.com/books/1361975680l/2657.jpg', 45.40),
      ('P102','The Chronicles OF Narnia','Journeys TO the END OF the world, fantastic creatures, AND epic battles BETWEEN good AND evil—what more could ANY reader ask FOR IN ONE book?...','https://images.gr-assets.com/books/1449868701l/11127.jpg', 44.50),
      ('P103','Gone WITH the Wind', 'Gone with the Wind is a novel written by Margaret Mitchell, first published in 1936.', 'https://images.gr-assets.com/books/1328025229l/18405.jpg',44.50),
      ('P104','The Fault IN Our Stars','Despite the tumor-shrinking medical miracle that has bought her a few YEARS, Hazel has never been anything BUT terminal, her FINAL chapter inscribed upon diagnosis.','https://images.gr-assets.com/books/1360206420l/11870085.jpg',14.50),
      ('P105','The Giving Tree','Once there was a tree...and she loved a little boy.','https://images.gr-assets.com/books/1174210942l/370493.jpg',32.0),
      ('P106','The Da Vinci Code','An ingenious code hidden IN the works OF Leonardo da Vinci.A desperate race through the cathedrals AND castles OF Europe','https://images.gr-assets.com/books/1303252999l/968.jpg',14.50),
      ('P107','The Alchemist','Paulo Coelho''s masterpiece tells the mystical story OF Santiago, an Andalusian shepherd boy who yearns TO travel IN SEARCH OF a worldly treasure','https://images.gr-assets.com/books/1483412266l/865.jpg',12.0),
      ('P108','Charlotte''s Web','This beloved book BY E. B. White, author OF Stuart Little AND The Trumpet OF the Swan, IS a classic OF children''s literature','https://images.gr-assets.com/books/1439632243l/24178.jpg',14.0),
      ('P109','The Little Prince','Moral allegory AND spiritual autobiography, The Little Prince IS the most translated book IN the French language.','https://images.gr-assets.com/books/1367545443l/157993.jpg',16.50),
      ('P110','A Thousand Splendid Suns','A Thousand Splendid Suns IS a breathtaking story SET against the VOLATILE events OF Afghanistan''s LAST thirty years—from the Soviet invasion TO the reign OF the Taliban TO post-Taliban rebuilding—that puts the violence, fear, hope, AND faith OF this country IN intimate, human terms.','https://images.gr-assets.com/books/1345958969l/128029.jpg',15.50),
      ('P111','A Game OF Thrones','Here IS the FIRST volume IN George R. R. Martin’s magnificent CYCLE OF novels that includes A Clash OF Kings AND A Storm OF Swords.','https://images.gr-assets.com/books/1436732693l/13496.jpg',32.0),
      ('P112','The Book Thief','Nazi Germany. The country IS holding its breath. Death has never been busier, AND will be busier still.By her brother''s graveside, Liesel''s life IS changed WHEN she picks up a single OBJECT, partially hidden IN the snow.','https://images.gr-assets.com/books/1522157426l/19063.jpg',30.0),
      ('P113','One Flew Over the Cuckoo''s Nest','Tyrannical Nurse Ratched rules her ward IN an Oregon State mental hospital WITH a strict AND unbending ROUTINE, unopposed BY her patients, who remain cowed BY mind-numbing medication AND the threat OF electric shock therapy.','https://images.gr-assets.com/books/1516211014l/332613.jpg',23.0)
;
