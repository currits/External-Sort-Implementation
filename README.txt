Usage:
 	CreateRuns, DistributeRuns and MergeRuns all have main methods with use descriptions if run with no arguments.
	Otherwise follows usage outlined in assignment spec (Standard IO, mergeRuns manages run distribution so no call to distributeRuns needed)
Limitations: 
	Data of mostly duplicate elements may produce only only a single run (become sorted), in which case passing it on to mergeRuns will cause it to loop indefinitely, as merge runs expects more than one file with runs.