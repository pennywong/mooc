import sys
import json

def get_scores(sentiment_file):
	scores = {}
	for line in open(sentiment_file):
		term, score  = line.split("\t")
		scores[term] = int(score)
	return scores
	
def get_tweets(tweet_file):
	tweets = []
	for line in open(tweet_file):
		data = json.loads(line)
		if data.has_key('text'):
			tweets.append(data['text'])
	return tweets

def main():
	sentiment_file = sys.argv[1]
	tweet_file = sys.argv[2]
	scores = get_scores(sentiment_file)
	tweets = get_tweets(tweet_file)
	
	for tweet in tweets:
		terms = set(tweet.split())
		score = 0
		for term in terms:
			if scores.has_key(term):
				score += scores[term]
		print score
	
if __name__ == '__main__':
    main()
