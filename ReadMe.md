# GrayChat

A mildly questionable fallback widget for Casterlabs Caffeinated.

## The problem

Casterlabs allows, among other things, merging different livestream chat rooms. However, due to how Youtube's API works, it is inevitably bound to keep hitting an API request cap, especially as the service becomes more popular, taking the entire Youtube part of the service down with it.

## The solution (sort of)

GrayChat is somewhat questionable a 2-component solution to bypass this restriction. It is questionable precisely because it is a bypass of Youtube restrictions. It does not use the Youtube API - instead, through some local server magic, and through the use of a TamperMonkey script on your browser and a widget on Casterlabs, it basically streams what the Youtube chat window sees to Caffeinated.

Note that this program was improvised over the course of a few hours, so at this time all it does is mirror chat and nothing else.

## How it works

This is a two-part solution. GrayChatScraper relies on the user opening a pop-out stream chat window, which will then automatically start streaming that data to the other component, the Casterlabs Caffeinated plugin. The outcome should be a seamless stream for as long as the pop-out Youtube live comments window is open.

## Limitations:

- Due to a bug on Casterlabs, as of writing this Casterlabs does not stream the chat to custom text widgets. You must use the chat from the Docks section instead.
- Youtube emojis will not be rendered in messages, which can result in empty messages
- Only chat is mirrored, because it is the only thing I care about. If there is interest I might expand it to more things.
- Chat refreshes every second, which still makes it faster than the default behavior as I understand it. This is because I'm not bound by the API.

## Dangers / Why is it questionable?

This is not disallowed by Youtube to the best of my limited knowledge. From a user perspective, this simply relies on having a normal chat window open, and does not use a single extra byte of bandwidth to achieve multi-chatting.

As far as I am aware, this is a permissible thing to use, otherwise I would not have published it. That being said, it is potentially a bit of a gray zone (hence the name of the plugin).

Realistically, Youtube has no way to know from traffic whether you are using this plugin or not. Realistically, Youtube won't really care whether this is or is not used.

That being said, if you are a streamer in the top 0.0001% of Youtube and start using this and proselytizing its use while speaking ill of how the official way things are supposed, generally making a big deal out of it, I don't know if Youtube would respond positively to that.

Use at your own discretion.

## Downloading / Installing / Using it

Click on the [releases](https://github.com/MagmaGuy/GrayChat/releases) and follow the instructions there. They are quite simple.