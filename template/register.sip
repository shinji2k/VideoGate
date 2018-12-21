REGISTER sip:${sipServerSid}@${sipServerIpPort} SIP/2.0
Via: SIP/2.0/UDP ${srcIpPort}
From: <sip:${sipDeviceCode}@${fromIp}>;tag=185326220
To: <sip:${sipDeviceCode}@${toIp}>
Call-ID: ${session}
CSeq: ${cseq} REGISTER
Contact: <sip:${sipDeviceCode}@${proxyIpPort}>
Authorization: Capability algorithm="A:RSA/ECB/PKCS1,RSA/CBC/PKCS1;H:SHA1,MD5,SHA256;S:DES/ECB/PKCS5,DES/CBC/PKCS5,3DES/ECB/PKCS5,3DES/CBC/PKCS5,SCB2"
Max-Forwards: 70
Expires: 3600
Content-Length: 0

