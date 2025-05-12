create table clothing_item (
  id bigserial primary key not null,
  user_id bigint not null,
  image_id varchar(50) not null,
  name text,
  notes text not null,
  website_name varchar(50),
  website_url text not null,
  favourite boolean not null default false,
  created_on timestamptz not null default now()
);

