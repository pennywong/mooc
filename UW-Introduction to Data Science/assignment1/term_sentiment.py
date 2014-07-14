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
	
def get_term_scores(tweets, scores):
	term_scores = {}
	for tweet in tweets:
		terms = set(tweet.split())
		score = 0
		temp_terms = {}
		for term in terms:
			if scores.has_key(term):
				score += scores[term]
			elif temp_terms.has_key(term):
				temp_terms[term] += 1
			else:
				temp_terms[term] = 1
		
		for key in temp_terms:
			if term_scores.has_key(key):
				term_scores[key][0] += temp_terms[key]
				term_scores[key][1] += score
			else:
				term_scores[key] = [temp_terms[key], score]
	return term_scores

def main():
	sentiment_file = sys.argv[1]
	tweet_file = sys.argv[2]
	scores = get_scores(sentiment_file)
	tweets = get_tweets(tweet_file)
	term_scores = get_term_scores(tweets, scores)
	
	total_term = 0
	for term in term_scores:
		total_term += term_scores[term][0]

	for term in term_scores:
		values = term_scores[term]
		print term, values[0]*values[1]/float(total_term)
		
	
if __name__ == '__main__':
    main()
