## The description of tidy dataset 

------

This dataset is a cleaning data about [Human+Activity+Recognition+Using+Smartphones](http://archive.ics.uci.edu/ml/datasets/Human+Activity+Recognition+Using+Smartphones).

It only contains the average of the measurements on the mean and standard deviation for each measurement by each subject and each activity.

The description of the tidy dataset's fileds is as below:

Field		| Description	|
------		| -----------	|
Subject		| Who performed the activity. Its range is from 1 to 30. 
Activity	| What activity the subject perform. There are six activities: WALKING, WALKING_UPSTAIRS, WALKING_DOWNSTAIRS, SITTING, STANDING and LAYING
Features	| the average of the measurements on the mean and standard deviation for each measurement

An Example data:

Subject	| Activity			| tBodyAcc-mean()-X	| other features...
-------	| ----------------	| -----------------	| -----
1		| WALKING			| 0.277330758736842	| ...
1		| WALKING_UPSTAIRS	| 0.255461689622641	| ...
2		| WALKING			| 0.276426586440678	| ...


------

### How to get the tidy dataset from raw data

1. read features and activity labels files
2. construct a **select_features** vector that be used to filter out the unneed column (only select the column that its column name contains "mean()" and "std()") when reading the measurement data. It can reduce the memory usage.
3. read the train subject and activity file
4. replace the train activity by activity labels
5. read the train measurement data filtered by **select_features**. (read.table(file, **colClasses=select_features**))
6. set the train measurement data column name by the corresponding features (the selected column).
7. merge the train subject, activity and measurement data
8. do the same thing to test data(from 3 to 7)
9. union the train and test data
10. use **melt** and **dcast** function to calculate the average of each variable for each subject and each activity
11. write the result to a file