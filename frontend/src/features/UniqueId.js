/*
    Implementing using the UUID standard from RFC 4122, specifically under section 4.4.
 */
function generateUUID() {
    /*

    */
    let date = new Date().getTime() //Take the current timestamp
    if (typeof performance !== 'undefined' && typeof performance.now === 'function') { //See whether high-precision timestamp can be used
        date += performance.now() //Then use it
    }
    return 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, function(charPlaceHolder) { //Use the RFC 4122
        const random = (date + (Math.random() << 4)) % 16 | 0 //Add randomness to the timestamp, making the UUID independent of it.
        date >>= 1 // Bit shifting the hex value one to the right.
        return (charPlaceHolder === 'x' ? random : (random & 0x3 | 0x8)).toString(16) //Changing the original string based on current value is x or y
    })
}

export default function UniqueID() {
    const getID = () => generateUUID()

    return {
        getID,
    }
}