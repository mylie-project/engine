package mylie.time;

/**
 * Represents information about a single frame in a simulation or animation context.
 * This record encapsulates details about the frame ID, simulation modifier,
 * time deltas, and timestamps related to the simulation and real-time.
 *
 * @param frameId      The unique identifier for the frame.
 * @param simMod       A simulation modifier that might adjust simulation behavior.
 * @param delta        The time elapsed since the last frame in real-time.
 * @param deltaSim     The time elapsed since the last frame in simulation time.
 * @param time         The total elapsed time in real-time since the start.
 * @param timeSim      The total elapsed time in simulation time since the start.
 */
public record FrameTime(long frameId, float simMod, float delta, float deltaSim, long time, long timeSim) {
}

//Write a concise commit message from 'git diff --staged' output in the format `[EMOJI] [TYPE](file/topic): [description in {locale}]`. Use GitMoji emojis (e.g., ✨ → feat), present tense, active voice, max 120 characters, one line, no code blocks.
//If required make a list of messages in the same format
//---
//{diff}

