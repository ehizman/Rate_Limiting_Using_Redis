# Redis Rate Limiter Project

redis-rate-limiter application is built using springboot, redis and postgresql

## Features
Two APIs that allow a user to send inbound SMS and outbound SMS.
The detailed system requirements is in this [document](https://docs.google.com/document/d/1jZ8GZ5ppIP-ftSY7r8DBIyelLul3dyh8TAKBX9ckzVU/edit)
## Installation

Step 1: Clone repository

Step 2: Install dependencies: cd into the root folder and run the following command: `mvn install`

Step 3: Download and install your redis server. Start your redis server using the `redis -server` command on your terminal.

Step 4: Setup database: run on data dump in the folder
`schema.sql` on your psql console.

Step 4: Run the project