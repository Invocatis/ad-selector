# Thank you for taking the time to do this!

The goal of this challenge is to **implement part of an ad delivery system**. We're not interested in the system actually delivering video ads - we just want to see **how you approach writing code and solving problems**.

You can take as much time as you'd like for this challenge, so please consider your decisions carefully and pay attention to the quality of what you deliver.

Please ping us whenever you're done, and we'll schedule a 90 minute **code review session** with you- where you'll have the opportunity to present your code and explain your design choices and any tradeoffs that you made, etc.

## Language: Clojure

Since most new code at RPM is written in Clojure, we'd like you to try use Clojure for this challenge. You don't need to be a Clojure expert! Just let us know what your Clojure experience level is, and we'll evaluate your submission from that perspective.

You're a long-term Clojure pro? We'll be hoping to see idiomatic syntax, design, and a strong understanding of the Clojure core API.

You're new to Clojure and hoping to maybe use it professionally? We'll be looking mostly for potential: cross-language design decisions, documentation, general approach to problem solving, etc.

## How we deliver ads (roughly...)

We serve adverts on various websites. When serving these adverts, there are various constraints that affect which ones we choose to serve. One part of our codebase evaluates the available ads for a given request.

Here's how it goes:

- When we get a request, we know where this ad is embedded - we call this the channel, and it could be a news site, a cooking blog, fashion or travelling website, etc.
- Similarly, we have always several ads we can display, each with a bunch of constraints (e.g. we only run German speaking ads in Germany, we only show cooking ads in cooking channels).
- A single ad can only be served for a certain amount of time -- it always has a start date and an end date.
- Ads have also a limit of views. Each ad has a limit, but some channels have lower limits (e.g. we can display the new Nike ad 5000 times, but only 50 times on, for instance, the New York Times blog).

So for each request, we want to return a suitable ad. If more than one ad is suitable, then we can return any of the suitable ads. If there are no suitable ads, we will not provide an ad.

## Your challenge

- Implement a prototype of this ad-request-to-ad matching mechanism that can serve ads.
- The request may be for a specific ad id, or may be just asking for available ads.
- The request contains a reference of which channel is sending it.
- There is other information in the ad-request, which will be used to discriminate which ad to show (country, language, interests that the user may have defined in the channel, etc).

## What we care about

Since this is a prototype we don't actually care about real ads or campaigns -- the data you use should be completely mocked and can always be in memory.

Here's what we do care about:

- The code, documentation, and Git history -- this challenge is for us to get an idea of how you approach writing code and solving problems.
- The reasons behind your technical decisions -- it's important that you're ready to explain them since we'll be going through your solution in detail.
- That we always get suitable ads according to the constraints -- how do you make sure that your code does what you think it does?
- What questions you'll ask -- it's more than ok to drop us a line or even give us a call on skype if you have questions or want to discuss some approach with us. We are happy to discuss the problem with you, make something more clear in case it's not or discuss solutions.
- Finally, that we are able to run your application without having to ask you questions over email. Make use of the README file, and be especially verbose if you're going to use Windows -- we're mostly Linux and Mac users in RPM.


Hope you have fun :-)  
- Red Pineapple Media Engineering Team

# USAGE

This section shall serve as a brief overview of how to explore the adserve logic. This is not necessarily how it would be used in production, but rather just how you may see that it works.

## Setting up

Start up your repl. You should be put into the user namespace, but if not, switch to it.

To create 100 example ads in the database, run the following:
```clojure
(create-ads 100)
```

## Simple Usage

Each ad will have a language, country, a set of interests, and a start and end date. These are randomly generated, so they likely will not make much sense but will suffice.

Let's query the first ad:
```clojure
(action/exec action/query state [:query :ad {:id 1}])
; =>
; {:language :en,
;  :country :gb,
;  :interests #{:handball :cheerleading :table_football},
;  :start-date
;  #object[org.joda.time.DateMidnight 0x22ee73c "2018-11-23T00:00:00.000Z"],
;  :end-date
;  #object[org.joda.time.DateMidnight 0x34dc755 "2019-08-09T00:00:00.000Z"],
;  :id 1}
```
This is the ad I got when I ran through this. Let's try to match this ad:
```clojure
(action/exec matching/match state
  {:language :en :country :gb
   :interests {:handball 100, :cheerleading 100, :table_football 100}})
;=>
; {:language :en,
;  :country :gb,
;  :interests #{:handball :cheerleading :table_football},
;  :start-date
;  #object[org.joda.time.DateMidnight 0x22ee73c "2018-11-23T00:00:00.000Z"],
;  :end-date
;  #object[org.joda.time.DateMidnight 0x34dc755 "2019-08-09T00:00:00.000Z"],
;  :id 1,
;  :relevancy 1}
```

We can see that our matching returned our ad, with an additional field describe how relevant it is compared to our passed parameter. The interests parameter is a weighted map describing the importance of each interest.

When delivering an ad, we not only want a relevant ad, but also the side effect adding an entry to the database logging the service. This can be done as follows:
```clojure
(action/exec action/serve-ad state
  {:channel-id 1
   :language :en :country :gb
   :interests {:handball 100, :cheerleading 100, :table_football 100}})
```

We can then verify the service is entered into the database:
```clojure
(action/exec action/query state
  [:query :services {}])
```

Finally, we want to be able to limit how many times an ad can be delivered, and delivered to a certain channel. This is done as follows:

```clojure
(action/exec action/execute state
  [:create [:limit 1] ;[:limit ad-id]
    {:entry {:channel 1 :times 100}}])
```

This should hopefully serve as a cursory introduction. I hope to go over the whole usage more specifically during code review.
