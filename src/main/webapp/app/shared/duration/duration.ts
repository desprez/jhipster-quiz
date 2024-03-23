const ISO_DURATION_REGEX = /^P(?:(?:(\d+)Y)?(?:(\d+)M)?(?:(\d+)D)?(?:T(?:(\d+)H)?(?:(\d+)M)?(?:(\d+)S)?)?|(?:(\d+)W))$/;

export class Duration {
  years: number;
  months: number;
  days: number;
  hours: number;
  minutes: number;
  seconds: number;
  weeks: number;

  constructor(duration: string) {
    [this.years, this.months, this.days, this.hours, this.minutes, this.seconds, this.weeks] = parseIsoDuration(duration);
  }

  toString() {
    let duration = 'P';
    if (this.years) {
      duration += `${this.years}Y`;
    }
    if (this.months) {
      duration += `${this.months}M`;
    }
    if (this.days) {
      duration += `${this.days}D`;
    }
    if (this.hours || this.minutes || this.seconds) {
      duration += 'T';
      if (this.hours) {
        duration += `${this.hours}H`;
      }
      if (this.minutes) {
        duration += `${this.minutes}M`;
      }
      if (this.seconds) {
        duration += `${this.seconds}S`;
      }
    }
    if (this.weeks) {
      duration += `${this.weeks}W`;
    }
    return duration === 'P' ? 'PT0S' : duration;
  }
}

export function parseIsoDuration(duration: string) {
  const match = RegExp(ISO_DURATION_REGEX).exec(duration);
  if (!match) {
    throw new Error(`Invalid duration: ${duration}`);
  }

  return [
    /* years */ Number(match[1]) || 0,
    /* months */ Number(match[2]) || 0,
    /* days */ Number(match[3]) || 0,
    /* hours */ Number(match[4]) || 0,
    /* minutes */ Number(match[5]) || 0,
    /* seconds */ Number(match[6]) || 0,
    /* weeks */ Number(match[7]) || 0,
  ];
}
