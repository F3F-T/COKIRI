import React, {useState, useEffect, useMemo, useCallback} from 'react';
import Image from 'react-bootstrap/Image'
import myImage from "../img/cokkiriLogo.png"

const KiriKiriCategoryRoundImage = ({props}) => {
    return (
        <>
            <Image src={props} className='img-fluid rounded-circle' />
        </>
    );
}

export default KiriKiriCategoryRoundImage;