import sys
import json

def get_tweets(tweet_file):
	tweets = []
	for line in open(tweet_file):
		data = json.loads(line)
		if data.has_key('text'):
			tweets.append(data['text'])
	return tweets

def main():
	tweet_file = sys.argv[1]	
	tweets = get_tweets(tweet_file)
	
	total_terms = 0
	term_records = {}
	for tweet in tweets:
		terms = tweet.split()
		for term in terms:		
			total_terms += 1
			if term_records.has_key(term):
				term_records[term] += 1
			else:
				term_records[term] = 1
	
	for term in term_records:
		print term, term_records[term]/float(total_terms)
	
if __name__ == '__main__':
    main()
