import styles from "../../styles/talk/talkList.module.scss";
import React from "react";
import MyPage from "../MyPageSet";


interface keyProps {
    keys: number;
}


const Message = (key: keyProps) => {

    const message: string[] = ["보낸사람 : 이거 얼마", "받은 사람 : 삼천원",
        "보낸사람 : 이거 얼마",  "받은 사람 : 삼천원",
        "보낸사람 : 이거 얼마", "받은 사람 : 삼천원",
        "보낸사람 : 이거 얼마", "받은 사람 : 삼천원"
    ]
    //

    const objectMessage = [{
        keys: 1,
        message: ["보낸 사람 : 이거 얼마", "받은 사람 : 삼천원",
            "보낸 사람 : 이거 얼마", "받은 사람 : 삼천원",
            "보낸 사람 : 이거 얼마", "받은 사람 : 삼천원",
            "보낸 사람 : 이거 얼마", "받은 사람 : 삼천원",
            "보낸 사람 : 이거 얼마", "받은 사람 : 삼천원",
            "보낸 사람 : 이거 얼마", "받은 사람 : 삼천원",
            "보낸 사람 : 이거 얼마", "받은 사람 : 삼천원",
            "보낸 사람 : 이거 얼마", "받은 사람 : 삼천원",
            "보낸 사람 : 이거 얼마", "보낸 사람 : 삼천원"
        ]
    },
        {
            keys: 2,
            message: ["보낸 사람 : 이거 얼마", "받은 사람 : 삼천원",
                "보낸 사람 : 이거 얼마", "받은 사람 : 삼천원",
                "보낸 사람 : 이거 얼마", "받은 사람 : 삼천원",
                "보낸 사람 : 이거 얼마", "받은 사람 : 삼천원"
            ]
        },
        {
            keys: 3,
            message: ["보낸 사람 : 이거 얼마", "받은 사람 : 삼천원",
                "보낸 사람 : 이거 얼마", "받은 사람 : 삼천원",
                "보낸 사람 : 이거 얼마", "받은 사람 : 삼천원",
                "보낸 사람 : 이거 얼마", "받은 사람 : 삼천원",

            ]
        },


    ]

    console.log("키 값")
    console.log("키키키킼",key.keys);

    const realKey = key.keys;
    // axios.get( url/chatroom/realKey=1)



    // <p className={styles.sendContent}>{singleMessage.slice(8,-1)}</p>
    return (
        <>
            {objectMessage[realKey-1].message.map((singleMessage: string) => (
                singleMessage.includes("보낸 사람") ?
                    <div className={styles.send}>
                        <div className={styles.sendTitle}>받은 쪽지</div>
                        <p className={styles.sendContent}>{singleMessage.substring(7,20)}</p>
                    </div> :
                    <div className={styles.receive}>
                        <div className={styles.receiveTitle}>보낸 쪽지</div>
                        <p className={styles.receiveContent}>{singleMessage.slice(7,20)}</p>
                    </div>
            ))}

        </>
    )
}
export default Message;