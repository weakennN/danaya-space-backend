create table users (
  id bigserial primary key not null,
  email text unique not null,
  password text not null,
  created_on timestamptz default now() not null
);

create table clothing_items (
  id bigserial primary key not null,
  user_id bigint not null references users(id),
  image_id varchar(50) not null,
  name text,
  notes text not null,
  website_name varchar(50),
  website_url text not null,
  favourite boolean not null default false,
  created_on timestamptz not null default now()
);

select * from users;
