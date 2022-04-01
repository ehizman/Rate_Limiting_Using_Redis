# How to Import From Your Local PostgreSQL Database into a Heroku PostgreSQL Database

### Introduction

Often times, you will need to migrate data from one [PostgreSQL](https://www.postgresql.org) database to another. The Heroku [PG Backups](https://devcenter.heroku.com/articles/heroku-postgres-backups) tool that you can use to export to 
and import from external PostgreSQL databases.

In completing this post, you will learn how to use the PG Backup tool to import data from your local PostgreSQL database
to your Heroku PostgreSQL database.

### Prerequisites
It is important to note the following before continuing with this post:
1. You should have an application already running on Heroku. You can visit this [resource](https://devcenter.heroku.com/articles/github-integration) to learn how to deploy your applications to Heroku
2. You should have the Heroku PostgreSQL addon added to your application. YOu can also visit this [resource](https://elements.heroku.com/addons/heroku-postgresql)
to learn about Heroku PostgreSQL addon and the available plans.
3. You should have an AWS (Amazon Web Service) account. You can create a free account by clicking on this [link](https://aws.amazon.com/free/?all-free-tier.sort-by=item.additionalFields.SortRank&all-free-tier.sort-order=asc&awsf.Free%20Tier%20Types=*all&awsf.Free%20Tier%20Categories=*all).
4. You should have the AWS CLI(Command Line Interface) installed on your computer.  This [resource](https://docs.aws.amazon.com/cli/latest/userguide/getting-started-install.html) explains how to download the AWS CLI for your Linux, Mac or Windows machine.
5. You should have PostgreSQL installed on your computer, and you must have data in your local PostgrSQL database that you want to import into a Heroku PostgreSQL database.
This [resource](https://www.postgresql.org/docs/current/backup-dump.html) provides information how to install PostgreSQL on your local machine

### Getting Started

The process of importing data from your local PostgreSQL to your Heroku PostgreSQL Database is divided into the following
processes:
1. Create the dump file.
2. Upload your dump file into an [Amazon S3 Bucket](https://aws.amazon.com/s3/).
3. Import your dump file to a Heroku PostgreSQL Database.

### How to create a database dump file

Dumping is a database backup technique. During dumping, a database **dump file** is created. A dump file is an SQL file that mirrors the state of the database at the time when the backup was done.
The dump file contains SQL commands, which when fed back to the same server as the database from which it was generated, restores the state of the database.

You can create a database dump file using the open source [pg_dump](https://www.postgresql.org/docs/current/backup-dump.html) tool.
PostgreSQL provides pg_dump as a utility program. It is available for use once you install PostgreSQL on your computer.
You can create a dump file with the `pg dump` tool using the `pg_dump` command as follows:
`pg_dump -Fc --no-acl --no-owner -h localhost -U <postgres user account> --format=c <name of postgres database> > <name of dump file>.dump`

To avoid prompted for the password of the user account that you provided in the above command, you can export the password as a system variable using the following command:
`export PGPASSWORD=<your postgreSQL user password>`
// add picture

In the screenshot above, the name of my local database is `postgres`, the name of my postgres user is also `postgres`, and
name of my dump file is `redis-rate-limiter`.

> Notice the `--format=c` part of the above command. This creates the dump file in a format that can be restored by `pg restore`
utility when trying to restore the database state (the `pg restore` utility restores the dump file to a target database using the specified format.) If the `--format=c`
command is not used, the dump file created by `pg dump` can be read as text files. Failure to attach the `--format=c` command results in the following error when you try to do the final restoration with `pg restore`:
```bash
  pg_restore: error: did not find magic string in file header
```

After creating your dump file, you can now upload your dump file to an [Amazon S3 Bucket](https://aws.amazon.com/s3/).

### How to upload your dump file into an Amazon s3 Bucket

> To follow the preceding steps, you must first have an AWS account, if you do not have an AWS account, you can create one [here](https://aws.amazon.com/free/?all-free-tier.sort-by=item.additionalFields.SortRank&all-free-tier.sort-order=asc&awsf.Free%20Tier%20Types=*all&awsf.Free%20Tier%20Categories=*all)

To upload to a s3 Bucket, you can follow the steps listed below:
1. Login to your [AWS console](https://us-east-1.console.aws.amazon.com/console/home). This yields the following screen
// add picture of AWS console.
2. Select the s3 option, this yields a screen that displays all the buckets currently on your account.
// display image of all the buckets on your account.
3. To create a new bucket, click on the "**Create Bucket**" button. This leads to form where you:
   - add the name of the bucket under the "Bucket name" field.
   //add picture
   - select the AWS Region
   //add picture
   - uncheck the "**Block _all_ public access**" checkbox.
   //add picture.
   - finally, click on the "**Create Bucket**" button at the base of the form.
4. The new bucket that you created is now added to the list of buckets on your account. Select the new bucket, and click on the "Upload" button.
Choose the "**Add Files**" option and then navigate to the directory where the dump file is stored on your computer.
5. After adding the dump file, click on the "**Upload**" button at the base of your screen.
//attach upload dump file picture.
6. After the dump file has been uploaded, click on it and this should direct you to a screen from which you can copy your s3 URI(Uniform Resource Identifier)
// attach picture of copy your s3 URI.

Now that you have successfully created and uploaded to an s3 Bucket, the next thing that you want to do now is to actually restore your dump file to a Heroku PostgreSQL Database.

### How to Import your dump file to a Heroku PostgreSQL Database.

To import your dump file into a Heroku PostgreSQL Database:
- First, you need to create a [signed URL]() for your newly created s3 Bucket using the following command:
`aws s3 presign <your s3 URI>`. 
For this command to work you must first install the AWS CLI(Command Line Interface). This [resource](https://docs.aws.amazon.com/cli/latest/userguide/getting-started-install.html) gives useful information on 
how to download and install the AWS CLI whether you are on a Mac, a Linux or a Windows computer.
- After generating your signed URL, you can now run the heroku `pg:backups:restore` command as follows:
`heroku pg:backups:restore '<your s3 signed URL>' DATABASE_URL -a <the name of your heroku application>`
`DATABASE_URL` represents a config variable stored in your Heroku PostgreSQL database. It is usually in the form: `postgres://wgqnunlXXXXXXX:54aac798a088c29730XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX@ec2-18-214-XXX-XXX.compute-1.amazonaws.com:5432/d32h4ffXXXXXXX`.
You can check that you have a Heroku PostgreSQL addon attached to your Heroku app by running the `heroku addons` command on your terminal.

If you have the Heroku PostgreSQL Database as an addon to your Heroku application, you can view your database config variables by running the `heroku config` command on your terminal.

> In the `heroku pg:backups:restore '<your s3 signed URL>' DATABASE_URL -a <the name of your heroku application>` command, make sure that you
> specify the name of your Heroku application after the `-a` flag. Failure to do this yields the following error:
```bash
  Error: Missing required flag:
  -a, --app APP  app to run command against
  See more help with --help
```

> It is also important to note that your signed URL should be enclosed within single quotes ('') if you are using a Linux or a Mac computer
> and double quotes ("") if you are using a Windows computer. This is because your signed URL may contain characters that may be flagged as invalid characters on your terminal.

- Enter the name of your Heroku application in the CLI as a confirmation that you want to perform a restore on the Heroku Postgres Database added to that application.
// display image below
-After entering the name of your Heroku application, Heroku completes the restore and responds with the following message on your CLI.
// add restore complete image.

You can now chek your Heroku PostgreSQL Dashboard to confirm that your import has indeed happened.

> Now, you can return to your AWS account and delete the s3 bucket which you created. This is a precaution since you did not block public access to the bucket
and since you have successfully migrated your data to Heroku PostgreSQL.

Conclusion

In this post you have learnt how to create a PostgreSQL database dump file and how to import data from your dump file into a Heroku PostgreSQL Database.

