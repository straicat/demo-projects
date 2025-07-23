import asyncio

import litellm
from stagehand import Stagehand, StagehandConfig


async def main():
    # litellm._turn_on_debug()
    sh = Stagehand(StagehandConfig(
        model_name="openai/gpt-4.1-mini",
        local_browser_launch_options={
            "cdp_url": "http://127.0.0.1:9222",
        }
    ), env="LOCAL")

    try:
        await sh.init()
        page = sh.page
        await page.goto("https://esf.fang.com/")
        await page.act("fill search input with 利泽西园")
        await page.act("click 搜索 button")
        houses = await page.extract("extract all house info, include title,area,total_price,unit_price", schema=Houses)
        for house in houses.list_of_houses:
            print(house)
    finally:
        await sh.close()

from litellm import BaseModel
class House(BaseModel):
    title: str
    area: str
    total_price: str
    unit_price: str
class Houses(BaseModel):
    list_of_houses: list[House]


if __name__ == '__main__':
    asyncio.run(main())
