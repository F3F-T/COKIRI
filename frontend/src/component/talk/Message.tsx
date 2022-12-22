import styles from "../../styles/talk/talkList.module.scss";
import React from "react";
import MyPage from "../MyPageSet";


interface keyProps {
    keys: number;
}


const Message = (key: keyProps) => {

    const message: string[] = ["보낸사람 : 이거 얼마", "받은 사람 : 삼천원",
        "보낸사람 : 이거 얼마", "받은 사람 : 삼천원",
        "보낸사람 : 이거 얼마", "받은 사람 : 삼천원",
        "보낸사람 : 이거 얼마", "받은 사람 : 삼천원"
    ]

    const objectMessage = [{
        keys: 1,
        message: ["111111보낸사람 : 이거 얼마", "받은 사람 : 삼천원",
            "보낸사람 : 이거 얼마", "받은 사람 : 삼천원",
            "보낸사람 : 이거 얼마", "받은 사람 : 삼천원",
            "보낸사람 : 이거 얼마", "받은 사람 : 삼천원"
        ]
    },
        {
            keys: 2,
            message: ["22222보낸사람 : 이거 얼마", "받은 사람 : 삼천원",
                "보낸사람 : 이거 얼마", "받은 사람 : 삼천원",
                "보낸사람 : 이거 얼마", "받은 사람 : 삼천원",
                "보낸사람 : 이거 얼마", "받은 사람 : 삼천원"
            ]
        },
        {
            keys: 3,
            message: ["3333333보낸사람 : 이거 얼마", "받은 사람 : 삼천원",
                "보낸사람 : 이거 얼마", "받은 사람 : 삼천원",
                "보낸사람 : 이거 얼마", "받은 사람 : 삼천원",
                "보낸사람 : 이거 얼마", "받은 사람 : 삼천원"
            ]
        },


    ]

    console.log("키 값")
    console.log(key.keys);

    const realKey = key.keys;
    // axios.get( url/chatroom/realKey=1)




    return (
        <>
            <div className={styles.send}>
                <div className={styles.sendTitle}>받은 쪽지</div>
                {/*<input className={styles.sendContent} type={"text"} />*/}
                {objectMessage[realKey-1].message.map((singleMessage: string) => (
                    <p>{singleMessage}</p>
                ))}
            </div>
            <div className={styles.receive}>
                <div className={styles.receiveTitle}>보낸 쪽지</div>
                {/*<input className={styles.receiveContent} type={"text"} />*/}
            </div>
        </>
    )
}
export default Message;