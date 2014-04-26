require(reshape2)

# read the data by name(train or test)
read_data <- function(name) {
	path = paste("UCI HAR Dataset/", name, "/", sep="")
	
	# read subject
	file <- paste(path, "subject_", name, ".txt", sep="")
	subject <- read.table(file, col.names=c("Subject"))
	
	# read activity
	file <- paste(path, "y_", name, ".txt", sep="")
	activity <- read.table(file, col.names=c("Activity"))
	# replace the activity by activity label
	activity[,1] = factor(x=activity[,1], labels=activity_label[,2])
	
	# read the data set
	file <- paste(path, "X_", name, ".txt", sep="")
	# just read the needed column
	data <- read.table(file, colClasses=select_features)
	# set the column name
	names(data) <- sapply(features[,2][select_features=="numeric"], as.character)
	
	# bind the subject, activity and data
	data <- cbind(subject, activity, data)
	 
	return(data)
}


# read features
features <- read.table("UCI HAR Dataset/features.txt")
# read activity label
activity_label <- read.table("UCI HAR Dataset/activity_labels.txt")

# extracts only the measurements on the mean and standard deviation for each measurement
select_features <- rep("NULL", nrow(features))
select_features[grep("mean()", features[,2], fixed="TRUE")] = "numeric"
select_features[grep("std()", features[,2], fixed="TRUE")] = "numeric"

# read train data set
train <- read_data("train")
# read test data set
test <- read_data("test")

# union the train and test data set
uci_data <- rbind(train, test)

# calculate the average of each variable for each activity and each subject
molten = melt(uci_data, id = c("Subject", "Activity"))
tidy_data <- dcast(molten, Subject + Activity ~ variable, mean)

# write the result to a file
write.table(tidy_data, file="tidy_dataset.txt")