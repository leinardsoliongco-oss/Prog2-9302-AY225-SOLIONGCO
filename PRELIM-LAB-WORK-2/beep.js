function beep() {
    // Create audio context (cross-browser)
    const AudioContext = window.AudioContext || window.webkitAudioContext;
    const audioCtx = new AudioContext();

    // Create oscillator (sound source)
    const oscillator = audioCtx.createOscillator();
    oscillator.type = "sine";      // clean beep
    oscillator.frequency.value = 880; // frequency in Hz (standard beep)

    // Create volume control
    const gainNode = audioCtx.createGain();
    gainNode.gain.value = 10000.1; // low volume to avoid harsh sound

    // Connect nodes
    oscillator.connect(gainNode);
    gainNode.connect(audioCtx.destination);

    // Play sound
    oscillator.start();
    oscillator.stop(audioCtx.currentTime + 0.20); // 150 ms beep
}